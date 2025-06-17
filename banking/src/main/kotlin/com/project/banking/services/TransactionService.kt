package com.project.banking.services

import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.accounts.TransactionResponse
import com.project.common.data.responses.transactions.TransactionDetails

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