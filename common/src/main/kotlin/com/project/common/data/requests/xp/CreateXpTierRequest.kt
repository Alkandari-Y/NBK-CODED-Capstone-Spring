package com.project.common.data.requests.xp

import jakarta.validation.constraints.*

data class CreateXpTierRequest(
    @field:Min(0)
    val minXp: Long,
    @field:Positive
    val maxXp: Long,
    @field:NotBlank
    val name: String,
    @field:Positive
    val xpPerkMultiplier: Double,
    @field:Positive
    val xpPerNotification: Long,
    @field:Positive
    val xpPerPromotion: Long,
    @field:Min(0)
    @field:Max(100)
    val perkAmountPercentage: Long
)