package com.project.common.data.requests.ble

import jakarta.validation.constraints.NotNull

data class BlueToothBeaconNotificationRequest(
    @field:NotNull
    val beaconId: Long,
    @field:NotNull
    val userId: Long
)