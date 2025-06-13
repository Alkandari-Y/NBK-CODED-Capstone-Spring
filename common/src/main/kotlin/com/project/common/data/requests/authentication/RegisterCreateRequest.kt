package com.project.common.data.requests.authentication

import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Length

data class RegisterCreateRequest(
    @field:NotBlank(message = "Username is required")
    @field:Length(min = 3, message = "Username is too short")
    val username: String,
    @field:NotBlank(message = "Password is required")
    @field:Length(min = 6, message = "Password is too short")
    @field:Pattern(regexp = """(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).*""", message = "Password is too simple")
    val password: String,

    @field:NotBlank(message = "Username is required")
    @field:Email(message = "Email is not valid")
    val email: String,

    @field:NotBlank(message = "Civil Id is required")
    val civilId: String,
)