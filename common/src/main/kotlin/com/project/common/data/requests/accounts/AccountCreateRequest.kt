package com.project.common.data.requests.accounts

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class AccountCreateRequest(
    @field:NotNull
    @field:DecimalMin(
        value = "100.00",
        inclusive = true,
        message = "Initial balance must be at least 100.00"
    )
    val initialBalance: BigDecimal,

    @field:NotBlank(message = "Account name cannot be blank")
    val name: String,

    @Min(value = 1, message = "Amount product ID must be at least 1")
    @field:NotNull(message = "Account type cannot be blank")
    val accountProductId: Long
)

