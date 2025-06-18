package com.project.common.data.requests.users

data class UserDeviceFBTokenRequest(
    val userId: Long,
    val firebaseToken: String
)

