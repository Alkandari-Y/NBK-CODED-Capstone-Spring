package com.project.banking.services

import com.project.common.exceptions.accounts.AccountVerificationException
import com.project.banking.entities.AccountEntity
import com.project.banking.mappers.toEntity
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.AccountRepository
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

const val MAX_ACCOUNT_LIMIT = 3

@Service
class AccountServiceImpl(
    private val accountProductRepository: AccountProductRepository,
    private val accountRepository: AccountRepository,
    private val mailService: MailService,
): AccountService {

    @CachePut(value = ["accountsByUserId"], key = "#userId")
    override fun getActiveAccountsByUserId(userId: Long): List<AccountDto> {
       return accountRepository.findByOwnerIdActive(userId)
    }

    @CacheEvict(value = ["accountsByUserId"], key = "#userInfoDto.userId")
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
}