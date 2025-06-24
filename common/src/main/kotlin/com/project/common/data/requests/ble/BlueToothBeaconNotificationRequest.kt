package com.project.common.data.requests.ble

data class BlueToothBeaconNotificationRequest(
    val beaconId: String,
    val userId: Long
)