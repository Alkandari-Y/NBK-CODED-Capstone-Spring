package com.project.common.data.requests.ble

import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull

data class BleStoreLocationRecommendationDataRequest(
    @field:NotNull
    @field:Min(1L)
    val userId: Long,
    @field:NotNull
    @field:Min(1L)
    val businessPartnerId: Long
)
