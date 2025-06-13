package com.project.banking.services

import com.project.banking.entities.CategoryEntity
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.exceptions.accounts.InsufficientFundsException
import com.project.common.exceptions.accounts.InvalidTransferException
import com.project.banking.entities.TransactionEntity
import com.project.banking.repositories.AccountRepository
import com.project.banking.repositories.TransactionRepository
import com.project.common.data.responses.transactions.TransactionDetails
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.accounts.TransactionResponse
import com.project.common.enums.TransactionType
import com.project.common.enums.ErrorCode
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryService: CategoryService
): TransactionService {

    @Transactional
    override fun transfer(newTransaction: TransferCreateRequest, userIdMakingTransfer: Long): TransactionResponse {
        if (newTransaction.sourceAccountNumber == newTransaction.destinationAccountNumber) {
            throw InvalidTransferException(message="Cannot transfer to the same account.",  code = ErrorCode.INVALID_TRANSFER)
        }

        val sourceAccount = accountRepository.findByAccountNumber(newTransaction.sourceAccountNumber)
        val destinationAccount = accountRepository.findByAccountNumber(newTransaction.destinationAccountNumber)

        if (sourceAccount == null || destinationAccount == null) {
            throw AccountNotFoundException("One or both accounts not found.")
        }

        if (sourceAccount.active.not() || destinationAccount.active.not()) {
            throw InvalidTransferException(
                "Cannot transfer with inactive account.",
                code = ErrorCode.INVALID_TRANSFER
            )
        }

        if (sourceAccount.ownerId != userIdMakingTransfer) {
            throw InvalidTransferException(
                "Cannot transfer with another persons account.",
                code = ErrorCode.INVALID_TRANSFER
            )
        }
        val category = categoryService.getCategoryByName("personal") ?: categoryService.createCategory(
            CategoryEntity(
                name = "personal"
            )
        )
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
                type = newTransaction.type ?: TransactionType.TRANSFER
            )
        )

        val updatedSourceAccount = accountRepository.save(
            sourceAccount.copy(balance = newSourceBalance)
        )
        val updatedDestinationAccount = accountRepository.save(
            destinationAccount.copy(balance = newDestinationBalance)
        )
        return TransactionResponse(
            sourceAccount = updatedSourceAccount.accountNumber,
            destinationAccount = updatedDestinationAccount.accountNumber,
            transactionId = transaction.id!!,
            amount = newTransaction.amount.setScale(3),
            category = category?.name!!,
        )
    }

    override fun getTransactionsByAccount(accountNumber: String): List<TransactionDetails> {
        return transactionRepository.findRelatedTransactions(accountNumber)
    }

    override fun getAllTransactionByUserId(
        userId: Long
    ): List<TransactionDetails> {
        return transactionRepository.findAllByUserId(userId)
    }
}