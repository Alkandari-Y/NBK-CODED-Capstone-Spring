package com.project.common.data.requests.accounts

import java.math.BigDecimal
import java.time.LocalDate

data class UserAccountsResponse (
    val userId: Long,
    val firstName: String,
    val lastName: String,
    var dateOfBirth: LocalDate? = null,
    val salary: BigDecimal,
    val nationality: String,
    val accounts: List<AccountBalanceCheck>
)

data class AccountBalanceCheck(
    val accountNumber: String,
    val balance: BigDecimal,
    val accountType: String,
    val accountId: Long
)