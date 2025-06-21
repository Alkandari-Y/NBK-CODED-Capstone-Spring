package com.project.common.data.responses.promotions

import java.time.LocalDate

data class PromotionResponse(
    val id: Long?,
    val name: String,
    val businessPartnerId: Long?,
    val type: Long?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val description: String,
    val storeId: Long?,
    val xp: Long?
)