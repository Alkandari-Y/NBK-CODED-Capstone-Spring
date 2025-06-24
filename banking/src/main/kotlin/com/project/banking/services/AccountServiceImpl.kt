package com.project.banking.services

import com.google.api.gax.rpc.ApiException
import com.project.common.exceptions.accounts.AccountVerificationException
import com.project.banking.entities.AccountEntity
import com.project.banking.entities.AccountProductEntity
import com.project.banking.mappers.toEntity
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.AccountRepository
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.enums.AccountOwnerType
import com.project.common.enums.AccountType
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal

const val MAX_ACCOUNT_LIMIT = 3

@Service
class AccountServiceImpl(
    private val accountProductRepository: AccountProductRepository,
    private val accountRepository: AccountRepository,
    private val mailService: MailService,
): AccountService {

    override fun getActiveAccountsByUserId(userId: Long): List<AccountDto> {
       return accountRepository.findByOwnerIdActive(userId)
    }

    override fun createClientAccount(
        accountRequest: AccountCreateRequest,
        userInfoDto: UserInfoDto
    )
    : AccountEntity {

        val accountProduct = accountProductRepository.findByIdOrNull(accountRequest.accountProductId)
            ?: throw AccountProductNotFoundException()

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
            val updated = userCashBackAccount.copy(balance = BigDecimal.valueOf(50))
            accountRepository.save(updated)
            // TODO: Add experience points and XP transaction record
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
        val existing = accountRepository.findFirstByOwnerIdAndAccountTypeOrderByIdAsc(
            userId,
            accountProducts.first().accountType)
        if (existing != null) return

        accountRepository.saveAll(
            accountProducts.map { AccountEntity(
                ownerId = userId,
                accountType = it.accountType,
                accountProduct = it
                )
            }
        )
    }
}