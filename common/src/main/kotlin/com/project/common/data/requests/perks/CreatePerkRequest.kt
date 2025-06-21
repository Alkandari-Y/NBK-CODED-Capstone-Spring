package com.project.common.data.requests.perks

import com.project.common.enums.RewardType
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CreatePerkRequest(
    @field:NotNull(message = "CASHBACK or DISCOUNT?")
    val type: RewardType,
    val minPayment: BigDecimal?,
    @field:NotNull(message = "Must give XP amount")
    val rewardsXp: Long,
    @field:NotNull
    @field:DecimalMin(value = "0.00", inclusive = false, message = "Perk amount must be above 0")
    val perkAmount: BigDecimal,
    val isTierBased: Boolean
)