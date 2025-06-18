package com.project.common.data.requests.categories

import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull

data class FavoriteCategoryRequest(
    @field:NotNull
    @field:Min(1)
    var categoryId: Long
)