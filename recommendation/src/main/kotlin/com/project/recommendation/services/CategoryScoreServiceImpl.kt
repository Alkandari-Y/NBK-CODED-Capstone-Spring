package com.project.recommendation.services

import com.project.common.data.requests.categoryScores.IncrementCategoryScoreRequest
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import com.project.common.exceptions.categories.CategoryScoreNotFoundException
import com.project.recommendation.entities.CategoryScoreEntity
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.CategoryScoreRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service


@Service
class CategoryScoreServiceImpl(
    private val bankServiceProvider: BankServiceProvider,
    private val categoryScoreRepository: CategoryScoreRepository
) : CategoryScoreService {

    override fun createUserCategoryScores(userId: Long) {
        val allCategories = bankServiceProvider.getAllCategories()
        val existingCategoryIds = categoryScoreRepository
            .findAllByUserId(userId).map { it.categoryId }.toSet()

        val newScores = allCategories
            .filter { it.id !in existingCategoryIds }
            .map { CategoryScoreEntity(
                    userId = userId,
                    categoryId = it.id,
                    frequency = 0 ) }

        if (newScores.isEmpty()) return

        categoryScoreRepository.saveAll(newScores)
    }

    override fun incrementCategoryFrequency(request: IncrementCategoryScoreRequest) {
        val categoryScore = categoryScoreRepository.findByCategoryIdAndUserId(
            userId = request.userId,
            categoryId = request.categoryId
        ) ?: throw CategoryScoreNotFoundException(
            "Category score not found for user ${request.userId} and category ${request.categoryId}")
        categoryScoreRepository.save(
            categoryScore.copy(frequency = categoryScore.frequency + 1))
    }

    override fun findAllCategoryScores(userId: Long): List<CategoryScoreEntity>? {
        return categoryScoreRepository.findAllByUserId(userId)
    }

    override fun findCategoryScoreById(userId: Long, categoryId: Long): CategoryScoreEntity? {
        return categoryScoreRepository.findByCategoryIdAndUserId(userId, categoryId)
    }

    override fun calculateCategoryScore(userId: Long, categoryId: Long) {
        TODO("Not yet implemented")
    }

    override fun getTop3Categories(userId: Long): List<Long> {
        TODO("Not yet implemented")
    }
}