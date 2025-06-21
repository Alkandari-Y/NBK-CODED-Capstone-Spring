package com.project.common.data.requests.promotions

import java.time.LocalDate

data class PromotionRequest(
    val name: String,
    val businessPartnerId: Long,
    val type: Long?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val description: String,
    val storeId: Long?,
    val xp: Long?,
)