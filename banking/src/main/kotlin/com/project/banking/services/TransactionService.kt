package com.project.banking.services

import com.project.banking.entities.AccountEntity
import com.project.common.data.requests.accounts.PaymentCreateRequest
import com.project.common.data.requests.accounts.TransferCreateRequest
import com.project.common.data.responses.transactions.PaymentDetails
import com.project.common.data.responses.transactions.TransactionDetails
import java.math.BigDecimal

interface TransactionService {
    fun getTransactionsByAccount(accountId: Long?, accountNumber: String?): List<TransactionDetails>
    fun getAllTransactionByUserId(userId: Long): List<TransactionDetails>

    fun transfer(
        newTransaction: TransferCreateRequest,
        userIdMakingTransfer: Long
    ): TransactionDetails

    fun purchase(userId: Long, purchaseRequest: PaymentCreateRequest): PaymentDetails
    fun awardCashback(source: AccountEntity, userId: Long, amount: BigDecimal)
}