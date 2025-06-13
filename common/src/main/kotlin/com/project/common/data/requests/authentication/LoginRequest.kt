package com.project.common.data.requests.authentication

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 50)
    val username: String,
    @field:NotBlank
    @field:Size(min = 6, max = 50)
    val password: String
)