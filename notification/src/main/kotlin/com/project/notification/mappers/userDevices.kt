package com.project.notification.mappers

import com.project.common.data.requests.firebase.UserDeviceFBTokenRequest
import com.project.notification.entities.UserDeviceEntity

fun UserDeviceFBTokenRequest.toEntity(userId: Long): UserDeviceEntity {
    return UserDeviceEntity(
        userId = userId,
        firebaseToken = this.firebaseToken
    )
}

