package com.project.common.data.responses.transactions

import java.math.BigDecimal
import java.time.Instant

data class TransactionDetails(
    val sourceAccountNumber: String,
    val destinationAccountNumber: String,
    val amount: BigDecimal,
    val createdAt: Instant,
    val category: String
)