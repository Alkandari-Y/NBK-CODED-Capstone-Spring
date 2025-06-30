package com.project.common.data.requests.recommendations

import com.project.common.data.responses.perks.PerkDto
import java.math.BigDecimal

data class RecommendedAccountProducts(
    val id: Long?,
    val name: String?,
    val description: String?,
    val accountType: String,
    val interestRate: BigDecimal,
    val minBalanceRequired: BigDecimal,
    val creditLimit: BigDecimal,
    val annualFee: BigDecimal,
    val minSalary: BigDecimal,
    val image: String?,
    val perks: List<PerkDto> = emptyList(),
    val categoryIds: Set<Long> = emptySet(),
    val categoryNames: Set<String> = emptySet(),
    val recommended: Boolean = false,
    val isOwned: Boolean = false
)