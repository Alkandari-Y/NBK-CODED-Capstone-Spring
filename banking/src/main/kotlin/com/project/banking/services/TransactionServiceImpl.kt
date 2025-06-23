package com.project.banking.services

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.CategoryEntity
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.exceptions.transactions.InsufficientFundsException
import com.project.common.exceptions.transactions.InvalidTransferException
import com.project.banking.entities.TransactionEntity
import com.project.banking.entities.XpHistoryEntity
import com.project.banking.mappers.toDto
import com.project.banking.mappers.toEntity
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.AccountRepository
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.banking.repositories.TransactionRepository
import com.project.banking.repositories.XpHistoryRepository
import com.project.common.data.requests.accounts.PaymentCreateRequest
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.transactions.PaymentDetails
import com.project.common.data.responses.transactions.TransactionDetails
import com.project.common.enums.AccountType
import com.project.common.enums.XpGainMethod
import com.project.common.enums.RewardType
import com.project.common.enums.TransactionType
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import com.project.common.exceptions.transactions.AccountLookupException
import com.project.common.exceptions.accounts.AccountNotActiveException
import com.project.common.exceptions.auth.InvalidCredentialsException
import com.project.common.exceptions.categories.CategoryNotFoundException
import jakarta.transaction.Transactional
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
    private val businessPartnerRepository: BusinessPartnerRepository
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
            sourceAccount.copy(balance = newSourceBalance)
        )
        val updatedDestinationAccount = accountRepository.save(
            destinationAccount.copy(balance = newDestinationBalance)
        )
        return TransactionDetails(
            transactionId = transaction.id!!,
            amount = newTransaction.amount.setScale(3),
            category = category.name!!,
            sourceAccountNumber = updatedSourceAccount.accountNumber,
            destinationAccountNumber = updatedDestinationAccount.accountNumber,
            createdAt = LocalDateTime.now(),
        )
    }

    @Transactional // THIS IS A WORK IN PROGRESS
    // right now it checks for if your card has perks
    // and assigns you the proper XP amount for it based on your tier
    // TODO: check for promotion
    // TODO: also check if notif was sent in last 24h
    override fun purchase(userId: Long, purchaseRequest: PaymentCreateRequest): PaymentDetails {
        if (purchaseRequest.type != TransactionType.PAYMENT) {
            throw InvalidTransferException("This is the PAYMENT endpoint.")
        }

        val (sourceAccount, businessAccount) = validateAndFetchAccounts(
            purchaseRequest.sourceAccountNumber,
            purchaseRequest.destinationAccountNumber,
            userId,
            purchaseRequest.amount
        )

        val category = businessPartnerRepository.findByAccountId(businessAccount.id!!)?.category
            ?: throw CategoryNotFoundException()

        val accountProduct = sourceAccount.accountProduct!!
        val perks = perkService.getAllPerksByAccountProduct(accountProduct.id!!)

        val matchedPerks = perks.filter { perk ->
            perk.categories.any { it.name == category.name } &&
                    purchaseRequest.amount >= (perk.minPayment ?: BigDecimal.ZERO)
        }

        var effectivePrice = purchaseRequest.amount.setScale(3)
        var totalXp = 0L
        var xpGainMethod: XpGainMethod? = null
        var xpRecord: XpHistoryEntity? = null

        // check for perks
        if (matchedPerks.isNotEmpty()) {
            xpGainMethod = XpGainMethod.PERK
            val bestPerk = matchedPerks.maxByOrNull { it.perkAmount }
            val perkAmount = bestPerk?.perkAmount ?: BigDecimal.ZERO

            when (bestPerk?.type) {
                RewardType.DISCOUNT -> effectivePrice = effectivePrice.subtract(perkAmount)
                RewardType.CASHBACK -> awardCashback(userId, perkAmount)
                else -> {} // no reward type, no reward, do nothing
            }

            val xpTier = xpService.getCurrentXpInfo(userId)!!.xpTier
            val multiplier = xpTier!!.xpPerkMultiplier

            val baseXp = bestPerk?.rewardsXp ?: 0
            totalXp += (multiplier * baseXp).toLong()
        }

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
        if (totalXp > 0L) {
            val xpInfo = xpService.getCurrentXpInfo(userId)!!

            xpRecord = XpHistoryEntity(
                amount = totalXp,
                gainMethod = xpGainMethod!!,
                transaction = transaction,
                category = category,
                xpTier = xpInfo.xpTier!!.toEntity(),
                userXp = xpInfo.toEntity(userId),
                account = sourceAccount,
                accountProduct = accountProduct
            )
            xpService.earnXP(xpRecord)
        }

        val newBalance = sourceAccount.balance.setScale(3).subtract(effectivePrice)
        accountRepository.save(sourceAccount.copy(balance = newBalance))

        return PaymentDetails(
            transactionId = transaction.id!!,
            sourceAccountNumber = transaction.sourceAccount?.accountNumber!!,
            destinationAccountNumber = transaction.destinationAccount?.accountNumber
                ?: purchaseRequest.destinationAccountNumber,
            amount = transaction.amount!!,
            category = transaction.category!!.name!!,
            createdAt = transaction.createdAt!!,
            xpHistoryRecord = xpRecord?.toDto()
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

    override fun awardCashback(userId: Long, amount: BigDecimal) {
        val cashbackAccount = accountRepository.findAllByOwnerId(userId)
            .firstOrNull { it.accountType == AccountType.CASHBACK }

        if (cashbackAccount == null) {
            throw AccountNotFoundException("No active cashback account found for user $userId")
        }

        val accountProduct = accountProductRepository.findByIdOrNull(cashbackAccount.accountProductId)
            ?: throw AccountProductNotFoundException()

        val newBalance = cashbackAccount.balance.setScale(3).add(amount.setScale(3))

        accountRepository.save(cashbackAccount.copy(balance = newBalance).toEntity(accountProduct))
    }
}