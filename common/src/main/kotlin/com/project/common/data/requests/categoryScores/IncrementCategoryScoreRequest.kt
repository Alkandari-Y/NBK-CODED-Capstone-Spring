package com.project.common.data.requests.categoryScores

data class IncrementCategoryScoreRequest(
    val userId: Long,
    val categoryId: Long
)