package com.project.recommendation.services

import com.project.common.data.requests.categoryScores.IncrementCategoryScoreRequest
import com.project.recommendation.entities.CategoryScoreEntity

interface CategoryScoreService {
    fun createUserCategoryScores(userId: Long)
    fun incrementCategoryFrequency(request: IncrementCategoryScoreRequest)
    fun findAllCategoryScores(userId: Long): List<CategoryScoreEntity>?
    fun findCategoryScoreById(userId: Long, categoryId: Long): CategoryScoreEntity?
    fun calculateCategoryScore(userId: Long, categoryId: Long)
}