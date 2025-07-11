package com.project.common.data.requests.accounts

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class AccountCreateRequest(
    @Min(value = 1, message = "Amount product ID must be at least 1")
    @field:NotNull(message = "Account type cannot be blank")
    val accountProductId: Long
)

