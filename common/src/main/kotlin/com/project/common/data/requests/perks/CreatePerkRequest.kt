package com.project.common.data.requests.perks

import com.project.common.enums.RewardType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CreatePerkRequest(
    @field:NotNull(message = "CASHBACK or DISCOUNT?")
    val type: RewardType,
    val minPayment: BigDecimal?,
    @field:Positive(message = "Must give XP amount")
    val rewardsXp: Long,
    @field:NotNull
    @field:Positive(message = "Perk amount must be above 0")
    val perkAmount: BigDecimal,
    val isTierBased: Boolean
)