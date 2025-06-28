package com.project.banking.services

import com.project.common.exceptions.accounts.AccountVerificationException
import com.project.banking.entities.AccountEntity
import com.project.banking.entities.AccountProductEntity
import com.project.banking.entities.CategoryEntity
import com.project.banking.entities.XpHistoryEntity
import com.project.banking.mappers.toDto
import com.project.banking.mappers.toEntity
import com.project.banking.mappers.toSummaryDto
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.AccountRepository
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.requests.ble.BleStoreLocationRecommendationDataRequest
import com.project.common.data.responses.accounts.UniqueUserProductsAndAllProducts
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.data.responses.accounts.AccountWithProductResponse
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.data.responses.ble.BleUserRecommendationInput
import com.project.common.enums.AccountOwnerType
import com.project.common.enums.AccountType
import com.project.common.enums.ErrorCode
import com.project.common.enums.XpGainMethod
import com.project.common.exceptions.APIException
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import com.project.common.exceptions.accountProducts.MultipleCashbackException
import com.project.common.exceptions.accounts.AccountLimitException
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.exceptions.businessPartner.BusinessNotFoundException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountServiceImpl(
    private val accountProductRepository: AccountProductRepository,
    private val accountRepository: AccountRepository,
    private val mailService: MailService,
    private val transactionService: TransactionService,
    private val xpService: XpService,
    private val categoryService: CategoryService,
    private val businessPartnerRepository: BusinessPartnerRepository
): AccountService {

    override fun getActiveAccountsByUserId(userId: Long): List<AccountDto> {
       return accountRepository.findByOwnerIdActive(userId)
    }

    override fun createClientAccount(
        accountRequest: AccountCreateRequest,
        userInfoDto: UserInfoDto
    ): AccountEntity {

        val accountsOwnedCount = accountRepository.findByOwnerIdActive(userInfoDto.userId).size

        if (accountsOwnedCount == 6) { throw AccountLimitException() }

        val accountProduct = accountProductRepository.findByIdOrNull(accountRequest.accountProductId)
            ?: throw AccountProductNotFoundException()

        if (accountProduct.accountType == AccountType.CASHBACK) { throw MultipleCashbackException() }

        if (accountProduct.name!!.contains("Business")) { throw AccountProductNotFoundException() }

        val account = accountRepository.save(accountRequest.toEntity(userInfoDto.userId, accountProduct))
        mailService.sendHtmlEmail(
            to = userInfoDto.username,
            subject = "Your bank account has been created",
            username = userInfoDto.username,
            bodyText = "Your account has been successfully created."
        )
        return account
    }


    override fun onBoardingCreateAccount(
        accountRequest: AccountCreateRequest,
        userInfoDto: UserInfoDto
    ): AccountEntity {

        val accountProduct = accountProductRepository.findByIdOrNull(accountRequest.accountProductId)
            ?: throw AccountProductNotFoundException()

        if (accountProduct.accountType != AccountType.CREDIT) {
            throw APIException(
                "Only credit accounts can be created through onboarding",
                httpStatus = HttpStatus.BAD_REQUEST,
                code = ErrorCode.INVALID_INPUT
            )
        }

        val userAccounts = accountRepository.findAllByOwnerId(userInfoDto.userId)

        val userCashBackAccount = accountRepository.findFirstByOwnerIdAndAccountTypeOrderByIdAsc(
            userInfoDto.userId,
            AccountType.CASHBACK
        )


        val hasSameProduct = userAccounts.any { it.accountProductId == accountProduct.id }

        if (hasSameProduct){
            throw APIException(
                "User already has this product",
                httpStatus = HttpStatus.BAD_REQUEST,
                code = ErrorCode.INVALID_INPUT
            )
        }

        if (userCashBackAccount != null) {
            val nbkAccount = businessPartnerRepository
                .findAll().firstOrNull { it.name == "National Bank of Kuwait" }!!.account!!

            val transaction = transactionService.awardCashback(
                source = nbkAccount,
                userId = userInfoDto.userId,
                amount = BigDecimal(10)
            )

            val category = categoryService.getCategoryByName("cashback")
                ?: categoryService.createCategory(CategoryEntity( name = "cashback"))

            val xpInfo = xpService.getCurrentXpInfo(userInfoDto.userId)!!
            val xpTier = xpInfo.xpTier!!.toEntity()
            val userXp = xpInfo.toEntity(userInfoDto.userId)

            val xpRecord = XpHistoryEntity(
                amount = 50L,
                gainMethod = XpGainMethod.ONBOARDING,
                transaction = transaction,
                category = category,
                xpTier = xpTier,
                userXp = userXp,
                account = transaction.sourceAccount,
                accountProduct = userCashBackAccount.accountProduct
            )
            xpService.earnXP(xpRecord)
        }

        val account = accountRepository.save(accountRequest.toEntity(userInfoDto.userId, accountProduct))

        return account
    }

    override fun closeAccount(accountNumber: String, user: UserInfoDto) {
        accountRepository.findByAccountNumber(accountNumber)?.apply {
            if (this.ownerId != user.userId) {
                throw AccountVerificationException()
            }

            if (this.isActive) {
                accountRepository.save(this.copy(isActive = false))
            }
            mailService.sendHtmlEmail(
                to = user.email,
                subject = "Your bank account has been closed",
                username = user.username,
                bodyText = "Your account has been successfully closed"
            )
        }
    }



    override fun getAccountById(accountId: Long): AccountEntity? {
        return accountRepository.findByIdOrNull(accountId)
    }

    override fun getByAccountNumber(accountNumber: String): AccountEntity? {
        return accountRepository.findByAccountNumber(accountNumber)
    }


    override fun getAllAccountsByUserId(userId: Long): List<AccountDto> {
        return accountRepository.findAllByOwnerId(userId)
    }

    override fun createNewClientPackage(userId: Long, accountProducts: List<AccountProductEntity>) {
        if (accountRepository.existsByOwnerId(userId)) return

        accountRepository.saveAll(
            accountProducts.map { AccountEntity(
                ownerId = userId,
                accountType = it.accountType,
                accountProduct = it
                )
            }
        )
    }

    override fun getClientUniqueProductsAndAccountProducts(request: BleStoreLocationRecommendationDataRequest): BleUserRecommendationInput {
        val uniqueUserProductIds = UniqueUserProductsAndAllProducts(
            accountRepository.getAllUniqueAccountProductIdsByUserId(request.userId)
        )

        val accountProducts = accountProductRepository.findAll()
        val businessPartners = businessPartnerRepository.findByIdOrNull(request.businessPartnerId)
            ?: throw BusinessNotFoundException()

        return BleUserRecommendationInput(
            userData = uniqueUserProductIds,
            allProducts = accountProducts
                .filter { uniqueUserProductIds.uniqueUserProducts.contains(it.id) }
                .map { it.toSummaryDto() },
            relatedPartnerSummary = businessPartners.toSummaryDto()
        )
    }

    override fun getAllAccountsInternal(userId: Long): List<AccountWithProductResponse> {
        val accounts = accountRepository.findByOwnerIdActive(userId)
        val uniqueProductIds = accounts.map { it.accountProductId }.toSet()
        val products = accountProductRepository.findAllById(uniqueProductIds).associateBy { it.id }

        return accounts.map { account ->
            val product = products[account.accountProductId] ?: throw AccountNotFoundException()
            AccountWithProductResponse(
                id = account.id,
                accountNumber = account.accountNumber,
                balance = account.balance,
                ownerId = account.ownerId,
                ownerType = account.ownerType,
                accountProduct = product.toDto(),
                accountType = account.accountType
            )
        }
    }
}