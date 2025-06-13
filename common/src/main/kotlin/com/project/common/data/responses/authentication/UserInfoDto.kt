package com.project.common.data.responses.authentication

data class UserInfoDto(
    val userId: Long,
    val isActive: Boolean,
    val email: String,
    val username: String
)
