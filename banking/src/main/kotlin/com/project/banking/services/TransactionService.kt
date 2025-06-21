package com.project.banking.services

import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.transactions.PaymentDetails
import com.project.common.data.responses.transactions.TransactionDetails

interface TransactionService {
    fun transfer(
        newTransaction: TransferCreateRequest,
        userIdMakingTransfer: Long
    ): TransactionDetails

    fun getTransactionsByAccount(accountNumber: String): List<TransactionDetails>
    fun getAllTransactionByUserId(userId: Long): List<TransactionDetails>

    fun purchase(userId: Long, purchaseRequest: TransferCreateRequest): PaymentDetails
}