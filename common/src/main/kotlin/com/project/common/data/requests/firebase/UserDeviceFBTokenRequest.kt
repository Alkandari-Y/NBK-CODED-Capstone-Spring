package com.project.common.data.requests.firebase

import jakarta.validation.constraints.NotBlank

data class UserDeviceFBTokenRequest(
    @field:NotBlank
    val firebaseToken: String
)