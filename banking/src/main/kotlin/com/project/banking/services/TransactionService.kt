package com.project.banking.services

import com.project.banking.transactions.dtos.TransactionDetails
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.accounts.TransactionResponse

interface TransactionService {
    fun transfer(
        newTransaction: TransferCreateRequest,
        userIdMakingTransfer: Long
    ): TransactionResponse

    fun getTransactionsByAccount(accountNumber: String): List<TransactionDetails>
    fun getAllTransactionByUserId(
        userId: Long
    ): List<TransactionDetails>
}