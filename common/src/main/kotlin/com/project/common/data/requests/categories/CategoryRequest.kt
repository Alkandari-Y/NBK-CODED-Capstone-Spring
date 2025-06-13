package com.project.common.data.requests.categories

import jakarta.validation.constraints.NotBlank

data class CategoryRequest(
    @field:NotBlank
    val name: String
)