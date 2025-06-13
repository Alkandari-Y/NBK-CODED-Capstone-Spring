package com.project.banking.services

import com.project.common.exceptions.accounts.AccountLimitException
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.exceptions.accounts.AccountVerificationException
import com.project.banking.entities.AccountEntity
import com.project.banking.mappers.toDto
import com.project.banking.repositories.AccountRepository
import com.project.common.data.requests.accounts.UpdateAccountRequest
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.exceptions.accounts.AccountNotActiveException
import jakarta.transaction.Transactional
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

const val MAX_ACCOUNT_LIMIT = 3

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val mailService: MailService,
): AccountService {

    @CachePut(value = ["accountsByUserId"], key = "#userId")
    override fun getActiveAccountsByUserId(userId: Long): List<AccountDto> {
       return accountRepository.findByOwnerIdActive(userId)
    }

    @CacheEvict(value = ["accountsByUserId"], key = "#userInfoDto.userId")
    override fun createClientAccount(
        accountEntity: AccountEntity,
        userInfoDto: UserInfoDto
    )
    : AccountEntity {

        val numOfCustomerAccount = accountRepository.getAccountCountByUserId(
            userId = userInfoDto.userId,
            accountType = accountEntity.accountType
        )

        if (numOfCustomerAccount >= MAX_ACCOUNT_LIMIT) {
            throw AccountLimitException()
        }

        val account = accountRepository.save(accountEntity.copy(ownerId = userInfoDto.userId))
        mailService.sendHtmlEmail(
            to = userInfoDto.username,
            subject = "Your bank account has been created",
            username = userInfoDto.username,
            bodyText = "Your account has been successfully created."
        )

        return account
    }

    @CacheEvict(value = ["accountsByUserId"], key = "#userInfoDto.userId")
    override fun closeAccount(accountNumber: String, user: UserInfoDto) {
        accountRepository.findByAccountNumber(accountNumber)?.apply {
            if (this.ownerId != user.userId) {
                throw AccountVerificationException()
            }

            if (this.active) {
                accountRepository.save(this.copy(active = false))
            }
            mailService.sendHtmlEmail(
                to = user.email,
                subject = "Your bank account has been closed",
                username = user.username,
                bodyText = "Your account has been successfully closed"
            )
        }
    }

    @Transactional
    override fun updateAccount(
        accountNumber: String,
        userId: Long,
        accountUpdate: UpdateAccountRequest
    ): AccountEntity {
        val accountToUpdate = accountRepository.findByAccountNumber(accountNumber)
            ?: throw AccountNotFoundException()


        if (!accountToUpdate.active) {
            throw AccountNotActiveException(accountNumber)
        }

        val updatedAccount = accountRepository.save(
            accountToUpdate.copy(
                name = accountUpdate.name
            )
        )

        return updatedAccount
    }

    override fun getAccountById(accountId: Long): AccountEntity? {
        return accountRepository.findByIdOrNull(accountId)
    }

    override fun getByAccountNumber(accountNumber: String): AccountEntity? {
        return accountRepository.findByAccountNumber(accountNumber)
    }


    override fun getAllAccountsByUserId(userId: Long): List<AccountEntity> {
        return accountRepository.findAllByOwnerId(userId)
    }
}