package com.project.common.data.responses.promotions

import com.project.common.enums.RewardType
import java.time.LocalDate

data class PromotionResponse(
    val id: Long?,
    val name: String,
    val businessPartnerId: Long?,
    val type: RewardType?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val description: String,
    val storeId: Long?,
    val xp: Long?
)