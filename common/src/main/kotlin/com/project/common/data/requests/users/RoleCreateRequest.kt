package com.project.common.data.requests.users

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class RoleCreateRequest (
    @field:NotBlank(message = "Role name cannot be blank")
    @field:Pattern(
        regexp = "^ROLE_.*",
        message = "Role name must start with 'ROLE_' and can contain alphanumeric characters, dashes, and underscores"
    )
    val name: String,
)
