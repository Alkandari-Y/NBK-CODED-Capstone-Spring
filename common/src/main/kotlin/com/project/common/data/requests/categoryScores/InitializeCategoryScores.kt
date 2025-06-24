package com.project.common.data.requests.categoryScores

import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull

data class InitializeCategoryScores(
    @field:NotNull
    @field:Min(1)
    val userId: Long
)