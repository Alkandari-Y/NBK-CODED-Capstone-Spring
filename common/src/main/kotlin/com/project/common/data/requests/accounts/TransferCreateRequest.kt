package com.project.common.data.requests.accounts

import com.project.common.enums.TransactionType
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class TransferCreateRequest(
    @field:NotBlank
    val sourceAccountNumber: String,
    @field:NotBlank
    val destinationAccountNumber: String,
    @field:NotNull
    @field:DecimalMin(
        value = "1.00",
        inclusive = true,
        message = "amount must must be at least 1.00"
    )
    val amount: BigDecimal,
    val type: TransactionType?,
    val category: String?
)
