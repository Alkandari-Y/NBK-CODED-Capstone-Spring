package com.project.common.data.requests.accountProducts

import com.project.common.enums.AccountType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import jakarta.validation.constraints.*

data class CreateAccountProductRequest(
    @field:NotBlank(message = "Name must not be blank")
    val name: String,

    @field:NotNull(message = "Account type must not be null")
    val accountType: AccountType,

    @field:NotBlank(message = "Description must not be blank")
    val description: String,

    @field:DecimalMin(value = "0.0", inclusive = false, message = "Interest rate must be positive")
    val interestRate: BigDecimal? = null,

    @field:DecimalMin(value = "0.0", inclusive = true, message = "Minimum balance must be non-negative")
    val minBalance: BigDecimal? = null,

    @field:DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be non-negative")
    val creditLimit: BigDecimal? = null,

    @field:DecimalMin(value = "0.0", inclusive = true, message = "Annual fee must be non-negative")
    val annualFee: BigDecimal? = null,

    @field:DecimalMin(value = "0.0", inclusive = true, message = "Minimum salary must be non-negative")
    val minSalary: BigDecimal? = null
)