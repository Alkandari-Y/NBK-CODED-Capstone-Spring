package com.project.banking.projections

import java.math.BigDecimal

interface AccountProductView {
    val id: Long
    val name: String
    val accountType: String
    val interestRate: BigDecimal
    val minBalanceRequired: BigDecimal
    val creditLimit: BigDecimal
    val annualFee: BigDecimal
    val minSalary: BigDecimal
    val image: String?
    val perks: List<PerkView>
}
