package com.project.recommendation.services

import com.project.common.data.requests.categoryScores.IncrementCategoryScoreRequest
import com.project.recommendation.entities.CategoryScoreEntity
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.CategoryScoreRepository
import org.springframework.stereotype.Service


@Service
class CategoryScoreService(
    private val bankServiceProvider: BankServiceProvider,
    private val categoryScoreRepository: CategoryScoreRepository
) {

    fun createUserCategoryScores(userId: Long, ): List<CategoryScoreEntity?> {
        val categoryScores = bankServiceProvider.getAllCategories()
            .map { category -> CategoryScoreEntity(
                userId = userId,
                categoryId = category.id,
                frequency = 0L
                )
            }
        return categoryScoreRepository.saveAll(categoryScores)
    }

    fun incrementCategoryScore(request: IncrementCategoryScoreRequest) {
        val categoryScore = categoryScoreRepository.findByCategoryIdAndUserId(
            userId = request.userId,
            categoryId = request.categoryId
        )
    }

    fun findAllCategoryScores(userId: Long): List<CategoryScoreEntity> {
        return categoryScoreRepository.findAllByUserId(userId)
    }

    fun findCategoryScoreById(userId: Long, categoryId: Long): CategoryScoreEntity? {
        return categoryScoreRepository.findByCategoryIdAndUserId(userId, categoryId)
    }
}