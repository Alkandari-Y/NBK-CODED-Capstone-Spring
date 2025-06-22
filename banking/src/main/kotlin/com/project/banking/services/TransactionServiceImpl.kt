package com.project.banking.services

import com.project.banking.entities.CategoryEntity
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.exceptions.transactions.InsufficientFundsException
import com.project.common.exceptions.transactions.InvalidTransferException
import com.project.banking.entities.TransactionEntity
import com.project.banking.repositories.AccountRepository
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.banking.repositories.TransactionRepository
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.transactions.PaymentDetails
import com.project.common.data.responses.transactions.TransactionDetails
import com.project.common.enums.TransactionType
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.transactions.AccountLookupException
import com.project.common.exceptions.accounts.AccountNotActiveException
import com.project.common.exceptions.auth.InvalidCredentialsException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryService: CategoryService
): TransactionService {

    @Transactional
    override fun transfer(newTransaction: TransferCreateRequest, userIdMakingTransfer: Long): TransactionDetails {
        if (newTransaction.sourceAccountNumber == newTransaction.destinationAccountNumber) {
            throw InvalidTransferException(message="Cannot transfer to the same account.",  code = ErrorCode.INVALID_TRANSFER)
        }

        val sourceAccount = accountRepository.findByAccountNumber(newTransaction.sourceAccountNumber)
        val destinationAccount = accountRepository.findByAccountNumber(newTransaction.destinationAccountNumber)

        if (sourceAccount == null || destinationAccount == null) {
            throw AccountNotFoundException("One or both accounts not found.")
        }

        if (sourceAccount.isActive.not() || destinationAccount.isActive.not()) {
            throw InvalidTransferException("Cannot transfer with inactive account.")
        }

        if (sourceAccount.ownerId != userIdMakingTransfer) {
            throw InvalidTransferException("Cannot transfer with another persons account.")
        }
        val category = categoryService.getCategoryByName("personal")
            ?: categoryService.createCategory(CategoryEntity( name = "personal"))

        val newSourceBalance = sourceAccount.balance.setScale(3).subtract(newTransaction.amount)
        val newDestinationBalance = destinationAccount.balance.setScale(3).add(newTransaction.amount)

        if (newSourceBalance < BigDecimal.ZERO) {
            throw InsufficientFundsException()
        }

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

    override fun purchase(userId: Long, purchaseRequest: TransferCreateRequest): PaymentDetails {
        val sourceAccount = accountRepository.findByAccountNumber(purchaseRequest.sourceAccountNumber)
            ?: throw AccountNotFoundException("Source account not found")

        if (!sourceAccount.isActive) {
            throw AccountNotActiveException(sourceAccount.accountNumber)
        }

        if (sourceAccount.ownerId != userId) { throw InvalidCredentialsException() }

        val newBalance = sourceAccount.balance.setScale(3).subtract(purchaseRequest.amount.setScale(3))
        if (newBalance < BigDecimal.ZERO) { throw InsufficientFundsException() }

        val category = categoryService.getCategoryByName(purchaseRequest.category!!)

        val businessAccount = accountRepository.findByAccountNumber(purchaseRequest.destinationAccountNumber)

        val transaction = transactionRepository.save(
            TransactionEntity(
                sourceAccount = sourceAccount,
                destinationAccount = businessAccount,
                amount = purchaseRequest.amount.setScale(3),
                category = category,
                transactionType = TransactionType.PAYMENT
            )
        )

        accountRepository.save(sourceAccount.copy(balance = newBalance))

        return PaymentDetails(
            transactionId = transaction.id!!,
            sourceAccountNumber = transaction.sourceAccount?.accountNumber!!,
            destinationAccountNumber = transaction.destinationAccount?.accountNumber ?: purchaseRequest.destinationAccountNumber,
            amount = transaction.amount!!,
            category = category?.name!!,
            createdAt = LocalDateTime.now()
        )
    }
}