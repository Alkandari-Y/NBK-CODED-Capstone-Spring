package com.project.common.data.responses.accounts

import com.project.common.enums.AccountType
import java.math.BigDecimal

data class AccountDto(
    val id: Long,
    val accountNumber: String,
    val name: String,
    val balance: BigDecimal,
    val active: Boolean,
    val ownerId: Long,
    val accountType: AccountType,
)