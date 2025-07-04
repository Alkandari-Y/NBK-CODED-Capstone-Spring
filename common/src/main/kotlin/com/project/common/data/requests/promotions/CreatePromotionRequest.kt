package com.project.common.data.requests.promotions

import com.project.common.enums.RewardType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.LocalDate

data class CreatePromotionRequest(
    @field:NotNull(message = "Name is required")
    val name: String,
    @field:NotNull(message = "Business partner ID is required")
    val businessPartnerId: Long,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    @field:NotNull(message = "Description is required")
    val description: String,
    val storeId: Long?
)