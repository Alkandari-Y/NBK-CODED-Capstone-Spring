package com.project.common.data.responses.perks

import com.project.common.data.responses.categories.CategoryDto
import com.project.common.enums.RewardType
import java.math.BigDecimal

data class PerkDto(
    val id: Long,
    val type: RewardType,
    val isTierBased: Boolean,
    val rewardsXp: Long,
    val perkAmount: BigDecimal,
    val minPayment: BigDecimal?,
    val accountProductId: Long,
    val categories: List<CategoryDto>
)