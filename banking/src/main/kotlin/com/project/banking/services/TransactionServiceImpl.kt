package com.project.banking.services

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.CategoryEntity
import com.project.banking.entities.TransactionEntity
import com.project.banking.entities.XpHistoryEntity
import com.project.banking.mappers.toDto
import com.project.banking.mappers.toEntity
import com.project.banking.providers.NotificationServiceProvider
import com.project.banking.providers.RecommendationServiceProvider
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.AccountRepository
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.banking.repositories.TransactionRepository
import com.project.common.data.requests.accountProducts.AccountProductRecDto
import com.project.common.data.requests.accounts.PaymentCreateRequest
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.transactions.PaymentDetails
import com.project.common.data.responses.transactions.TransactionDetails
import com.project.common.enums.AccountType
import com.project.common.enums.RewardType
import com.project.common.enums.TransactionType
import com.project.common.enums.XpGainMethod
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import com.project.common.exceptions.accounts.AccountNotActiveException
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.exceptions.auth.InvalidCredentialsException
import com.project.common.exceptions.businessPartner.BusinessNotFoundException
import com.project.common.exceptions.categories.CategoryNotFoundException
import com.project.common.exceptions.transactions.AccountLookupException
import com.project.common.exceptions.transactions.InsufficientFundsException
import com.project.common.exceptions.transactions.InvalidTransferException
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val accountProductRepository: AccountProductRepository,
    private val categoryService: CategoryService,
    private val perkService: PerkService,
    private val xpService: XpService,
    private val businessPartnerRepository: BusinessPartnerRepository,
    private val recommendationServiceProvider: RecommendationServiceProvider,
    private val notificationServiceProvider: NotificationServiceProvider
): TransactionService {
    override fun getTransactionsByAccount(accountId: Long?, accountNumber: String?): List<TransactionDetails> {
        if ((accountId == null && accountNumber == null) || (accountId != null && accountNumber != null)) {
            throw AccountLookupException()
        }

        return when {
            accountId != null -> {
                val account = accountRepository.findById(accountId).orElseThrow { AccountNotFoundException() }
                transactionRepository.findRelatedTransactionsByAccountNumber(account.accountNumber)
            }
            accountNumber != null -> transactionRepository.findRelatedTransactionsByAccountNumber(accountNumber)
            else -> emptyList()
        }
    }
    private val logger = LoggerFactory.getLogger(TransactionServiceImpl::class.java)

    override fun getAllTransactionByUserId(userId: Long): List<TransactionDetails> {
        return transactionRepository.findAllByUserId(userId)
    }

    @Transactional
    override fun transfer(newTransaction: TransferCreateRequest, userIdMakingTransfer: Long): TransactionDetails {
        if (newTransaction.type == TransactionType.PAYMENT) {
            throw InvalidTransferException("This is the TRANSFER endpoint, not the payment endpoint.")
        }

        val (sourceAccount, destinationAccount) = validateAndFetchAccounts(
            newTransaction.sourceAccountNumber,
            newTransaction.destinationAccountNumber,
            userIdMakingTransfer,
            newTransaction.amount
        )

        if (destinationAccount.ownerId != userIdMakingTransfer) {
            throw InvalidTransferException("Cannot transfer to an account that does not belong to you")
        }

        val sourceType = sourceAccount.accountProduct!!.accountType
        val destinationType = destinationAccount.accountProduct!!.accountType

        when {
            sourceType == AccountType.CREDIT ->
                throw InvalidTransferException("Cannot transfer from a credit account")
            destinationType == AccountType.CASHBACK ->
                throw InvalidTransferException("Cannot transfer to a cashback account")
            sourceType == AccountType.CASHBACK && destinationType != AccountType.DEBIT ->
                throw InvalidTransferException("Cashback can only transfer to debit")
        }

        val category = categoryService.getCategoryByName("personal")
            ?: categoryService.createCategory(CategoryEntity( name = "personal"))

        val newSourceBalance = sourceAccount.balance.setScale(3).subtract(newTransaction.amount)
        val newDestinationBalance = destinationAccount.balance.setScale(3).add(newTransaction.amount)

        val transaction = transactionRepository.save(
            TransactionEntity(
                sourceAccount = sourceAccount,
                destinationAccount = destinationAccount,
                amount = newTransaction.amount.setScale(3),
                category = category,
                transactionType = newTransaction.type ?: TransactionType.TRANSFER
            )
        )

        val updatedSourceAccount = accountRepository.save(
            sourceAccount.copy(balance = newSourceBalance))

        val updatedDestinationAccount = accountRepository.save(
            destinationAccount.copy(balance = newDestinationBalance))

        return TransactionDetails(
            transactionId = transaction.id!!,
            amount = newTransaction.amount.setScale(3),
            category = category.name!!,
            sourceAccountNumber = updatedSourceAccount.accountNumber,
            destinationAccountNumber = updatedDestinationAccount.accountNumber,
            createdAt = LocalDateTime.now(),
        )
    }

    @Transactional
    override fun purchase(userId: Long, purchaseRequest: PaymentCreateRequest): PaymentDetails {
        if (purchaseRequest.type != TransactionType.PAYMENT) {
            throw InvalidTransferException("This is the PAYMENT endpoint.")
        }

        val (sourceAccount, businessAccount) = validateAndFetchAccounts(
            purchaseRequest.sourceAccountNumber,
            purchaseRequest.destinationAccountNumber,
            userId,
            purchaseRequest.amount)

        val businessPartner = businessPartnerRepository.findByAccountId(businessAccount.id!!)
            ?: throw BusinessNotFoundException()

        val category = businessPartner.category
            ?: throw CategoryNotFoundException()

        val accountProduct = sourceAccount.accountProduct!!
        val perks = perkService.getAllPerksByAccountProduct(accountProduct.id!!)

        val matchedPerks = perks.filter { perk ->
            perk.categories.any { it.name == category.name } &&
                    purchaseRequest.amount >= (perk.minPayment ?: BigDecimal.ZERO) }

        var effectivePrice = purchaseRequest.amount.setScale(3)

        val xpInfo = xpService.getCurrentXpInfo(userId)!!
        val xpTier = xpInfo.xpTier!!.toEntity()
        val userXp = xpInfo.toEntity(userId)

        // check for active promos
        val promotions = businessPartner.id?.let {
            recommendationServiceProvider.getActivePromotionsByBusiness(it)
        } ?: emptyList()

        val sentNotification = notificationServiceProvider.getPromotionNotificationSentToUserForPartner(
            userId = userId,
            partnerId = businessPartner.id!!,
        )

        val matchedPromo = promotions.firstOrNull()

        if (matchedPerks.isNotEmpty()) {
            val bestPerk = matchedPerks.maxByOrNull { it.perkAmount }
            var percentageMultiplier = BigDecimal.ONE
            if (bestPerk != null && bestPerk.isTierBased) {
                percentageMultiplier = BigDecimal(xpTier.perkAmountPercentage!!)
                    .divide(BigDecimal(100))}
            val perkAmount = bestPerk?.perkAmount?.multiply(percentageMultiplier) ?: BigDecimal.ZERO

            when (bestPerk?.type) {
                RewardType.DISCOUNT -> effectivePrice = effectivePrice.subtract(perkAmount)
                RewardType.CASHBACK -> awardCashback(businessAccount, userId, perkAmount)
                else -> {} // no reward type, no reward, do nothing
            }
        }

        recommendationServiceProvider.incrementCategoryFrequency(userId, category.id!!)

        val transaction = transactionRepository.save(
            TransactionEntity(
                sourceAccount = sourceAccount,
                destinationAccount = businessAccount,
                amount = effectivePrice,
                category = category,
                transactionType = TransactionType.PAYMENT
            )
        )

        // earn xp if it was awarded
        val earnedXpRecords = mutableListOf<XpHistoryEntity>()

        if (matchedPromo != null) {
            val promoXp = xpInfo.xpTier!!.xpPerPromotion
            val promoXpRecord = XpHistoryEntity(
                amount = promoXp,
                gainMethod = XpGainMethod.PROMOTION,
                transaction = transaction,
                category = category,
                xpTier = xpTier,
                userXp = userXp,
                account = sourceAccount,
                accountProduct = accountProduct
            )
            xpService.earnXP(promoXpRecord)
            earnedXpRecords.add(promoXpRecord)
        }

        if (matchedPerks.isNotEmpty()) {
            val bestPerk = matchedPerks.maxByOrNull { it.perkAmount }
            val baseXp = bestPerk?.rewardsXp ?: 0
            val multiplier = xpInfo.xpTier!!.xpPerkMultiplier
            val perkXp = (multiplier * baseXp).toLong()

            val perkXpRecord = XpHistoryEntity(
                amount = perkXp,
                gainMethod = XpGainMethod.PERK,
                transaction = transaction,
                category = category,
                xpTier = xpTier,
                userXp = userXp,
                account = sourceAccount,
                accountProduct = accountProduct
            )
            xpService.earnXP(perkXpRecord)
            earnedXpRecords.add(perkXpRecord)
        }

        if (sentNotification != null) {
            val promoXp = xpInfo.xpTier!!.xpPerPromotion
            val promoXpRecord = XpHistoryEntity(
                amount = promoXp,
                gainMethod = XpGainMethod.NOTIFICATION,
                transaction = transaction,
                category = category,
                xpTier = xpTier,
                userXp = userXp,
                account = sourceAccount,
                accountProduct = accountProduct
            )
            xpService.earnXP(promoXpRecord)
            earnedXpRecords.add(promoXpRecord)
        }

        val newBalance = sourceAccount.balance.setScale(3).subtract(effectivePrice)
        accountRepository.save(sourceAccount.copy(balance = newBalance))

        // calculate account score and trigger rec if too low
        if (accountProduct.accountType != AccountType.CASHBACK) {
            val (score, transactionCount, xpMatchCount) =
                calculateAccountScore(userId, accountProduct.id!!)
                    ?: Triple(0.0, 0, 0)

            logger.info("Account product score: $score")

            if (score < 0.15) {
                val usersUniqueCards = accountRepository.findByOwnerIdActive(userId)
                    .map { it.accountProductId }.distinct()

                val recDto = AccountProductRecDto(
                    userId = sourceAccount.ownerId!!,
                    totalNumTransactions = transactionCount,
                    totalNumValidPerkPurchases = xpMatchCount,
                    accountScore = score,
                    currentAccountProductId = accountProduct.id!!,
                    currentAccountId = sourceAccount.id!!,
                    listOfOwnedUniqueAccountProductIds = usersUniqueCards
                )
                recommendationServiceProvider.triggerAccountScoreNotif(recDto)
            }
        }

        return PaymentDetails(
            transactionId = transaction.id!!,
            sourceAccountNumber = transaction.sourceAccount?.accountNumber!!,
            destinationAccountNumber = transaction.destinationAccount?.accountNumber
                ?: purchaseRequest.destinationAccountNumber,
            amount = transaction.amount!!,
            category = transaction.category!!.name!!,
            createdAt = transaction.createdAt!!,
            xpHistoryRecord = earnedXpRecords.map { it.toDto() }
        )
    }

    private fun validateAndFetchAccounts(
        sourceAccountNumber: String,
        destinationAccountNumber: String,
        userId: Long,
        amount: BigDecimal
    ): Pair<AccountEntity, AccountEntity> {
        if (sourceAccountNumber == destinationAccountNumber) {
            throw InvalidTransferException("Cannot transfer to the same account")
        }

        val sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
            ?: throw AccountNotFoundException("Source account not found")
        val destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber)
            ?: throw AccountNotFoundException("Destination account not found")

        if (!sourceAccount.isActive) {
            throw AccountNotActiveException(sourceAccount.accountNumber)
        }

        if (!destinationAccount.isActive) {
            throw AccountNotActiveException(destinationAccount.accountNumber)
        }

        if (sourceAccount.ownerId != userId) { throw InvalidCredentialsException() }

        if (amount <= BigDecimal.ZERO) {
            throw InvalidTransferException("Amount must be greater than zero")
        }

        val newSourceBalance = sourceAccount.balance.setScale(3).subtract(amount.setScale(3))
        if (newSourceBalance < BigDecimal.ZERO) { throw InsufficientFundsException() }

        return sourceAccount to destinationAccount
    }

    private fun calculateAccountScore(userId: Long, accountProductId: Long): Triple<Double, Int, Int>? {
        val windowStart = LocalDateTime.now().minusDays(30)

        val totalTransactions = transactionRepository.countRecentTransactionsByAccountProduct(
            userId = userId,
            accountProductId = accountProductId,
            after = windowStart
        )

        val matchedXpEvents = xpService.countPerkXpEvents(
            userId = userId,
            accountProductId = accountProductId,
            after = windowStart
        )

        if (totalTransactions == 0) return null // don't want zero division

        val score = matchedXpEvents.toDouble() / totalTransactions
        return Triple(
            score.coerceIn(0.0, 1.0),
            totalTransactions,
            matchedXpEvents)
    }

    override fun awardCashback(source: AccountEntity, userId: Long, amount: BigDecimal): TransactionEntity {
        val cashbackAccount = accountRepository.findAllByOwnerId(userId)
            .firstOrNull { it.accountType == AccountType.CASHBACK }

        if (cashbackAccount == null) {
            throw AccountNotFoundException("No active cashback account found for user $userId")
        }

        val accountProduct = accountProductRepository.findByIdOrNull(cashbackAccount.accountProductId)
            ?: throw AccountProductNotFoundException()

        val newBalance = cashbackAccount.balance.setScale(3).add(amount.setScale(3))
        val category = categoryService.getCategoryByName("cashback")
            ?: categoryService.createCategory(CategoryEntity( name = "cashback"))

        val destination = cashbackAccount.toEntity(accountProduct)

        val transaction = transactionRepository.save(
            TransactionEntity(
                sourceAccount = source,
                destinationAccount = destination,
                amount = newBalance,
                category = category,
                transactionType = TransactionType.TRANSFER
            ))

        accountRepository.save(destination.copy(balance = transaction.amount!!))

        return transaction
    }
}