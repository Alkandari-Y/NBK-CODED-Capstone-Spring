package com.project.common.data.responses.accounts

import java.math.BigDecimal


data class TransactionResponse(
    val sourceAccount: String, // account number
    val destinationAccount: String, // account number
    val transactionId: Long,
    val amount: BigDecimal,
    val category: String
)