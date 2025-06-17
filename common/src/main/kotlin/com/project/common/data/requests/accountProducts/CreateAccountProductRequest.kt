package com.project.common.data.requests.accountProducts

import com.project.common.enums.AccountType
import java.math.BigDecimal

data class CreateAccountProductRequest(
    val name: String,
    val accountType: AccountType,
    val interestRate: BigDecimal? = null,
    val minBalance: BigDecimal? = null,
    val creditLimit: BigDecimal? = null,
    val annualFee: BigDecimal? = null,
    val minSalary: Long? = null,
    val image: String,
)

