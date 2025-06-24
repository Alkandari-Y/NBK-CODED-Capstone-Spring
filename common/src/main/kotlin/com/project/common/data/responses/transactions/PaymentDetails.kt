package com.project.common.data.responses.transactions

import com.project.common.data.requests.xp.XpHistoryDto
import com.project.common.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentDetails(
    val transactionId: Long,
    val sourceAccountNumber: String,
    val destinationAccountNumber: String,
    val amount: BigDecimal,
    val createdAt: LocalDateTime,
    val category: String,
    var transactionType: TransactionType = TransactionType.PAYMENT,
    val xpHistoryRecord: List<XpHistoryDto>
)