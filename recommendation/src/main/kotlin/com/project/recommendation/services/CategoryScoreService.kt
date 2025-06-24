package com.project.recommendation.services

import com.project.common.data.requests.categoryScores.IncrementCategoryScoreRequest
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import com.project.recommendation.entities.CategoryScoreEntity
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.CategoryScoreRepository
import org.springframework.http.HttpStatus
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
        ) ?: throw APIException("category score not found for user ${request.userId} and category ${request.categoryId}",
            httpStatus = HttpStatus.NOT_FOUND,
            ErrorCode.CATEGORY_SCORE_NOT_FOUND
            )
        categoryScoreRepository.save(
            categoryScore.copy(frequency = categoryScore.frequency + 1))
    }

    fun findAllCategoryScores(userId: Long): List<CategoryScoreEntity> {
        return categoryScoreRepository.findAllByUserId(userId)
    }

    fun findCategoryScoreById(userId: Long, categoryId: Long): CategoryScoreEntity? {
        return categoryScoreRepository.findByCategoryIdAndUserId(userId, categoryId)
    }
}