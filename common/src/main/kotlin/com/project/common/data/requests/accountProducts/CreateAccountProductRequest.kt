package com.project.common.data.requests.accountProducts

import com.project.common.enums.AccountType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CreateAccountProductRequest(
    @field:NotBlank
    val name: String,

    @field:NotNull
    val accountType: AccountType,

    val interestRate: BigDecimal? = null,
    val minBalance: BigDecimal? = null,
    val creditLimit: BigDecimal? = null,
    val annualFee: BigDecimal? = null,
    val minSalary: BigDecimal? = null
)
