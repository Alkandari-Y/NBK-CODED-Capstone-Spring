package com.project.recommendation.services

import com.project.common.data.requests.categoryScores.IncrementCategoryScoreRequest
import com.project.common.exceptions.categories.CategoryScoreNotFoundException
import com.project.recommendation.entities.CategoryScoreEntity
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.CategoryScoreRepository
import java.time.Duration
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class CategoryScoreServiceImpl(
    private val bankServiceProvider: BankServiceProvider,
    private val categoryScoreRepository: CategoryScoreRepository,
    private val favCategoriesService: FavCategoriesService
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

    override fun findAllCategoryScores(userId: Long): List<CategoryScoreEntity> {
        return categoryScoreRepository.findAllByUserId(userId)
    }

    override fun findCategoryScoreById(userId: Long, categoryId: Long): CategoryScoreEntity {
        return categoryScoreRepository.findByCategoryIdAndUserId(userId, categoryId)
            ?: throw CategoryScoreNotFoundException()
    }


    override fun calculateCategoryScore(userId: Long, categoryId: Long): Double {
        val userFavCategories = favCategoriesService.findAllFavCategories(userId).map { it.id }
        val preferenceScore = if (userFavCategories.contains(categoryId)) 1.0 else 0.0

        val userCards = bankServiceProvider.getUserAccountProducts(userId)
        val staticPerkScore = if (
            userCards.any { it.accountProduct.perks.any { perk ->
                perk.categories.any { category -> category.id == categoryId } } }) 1.0 else 0.0

        val allCategories = bankServiceProvider.getAllCategories()
        val categoryNameToId = allCategories.associateBy({ it.name }, { it.id })

        val transactions = bankServiceProvider.getUserTransactions(userId)
        val lastTransaction = transactions
            .filter { val transactionCategoryId = categoryNameToId[it.category] ?: -1L
                transactionCategoryId == categoryId }.maxByOrNull { it.createdAt }

        val now = LocalDateTime.now()
        val historyScore = if (lastTransaction != null &&
            Duration.between(lastTransaction.createdAt, now).toDays() <= 60) 1.0 else 0.0

        val allCategoryScores = categoryScoreRepository.findAllByUserId(userId)
        val frequency = allCategoryScores.find { it.categoryId == categoryId }?.frequency ?: 0
        val maxFrequency = allCategoryScores.maxOfOrNull { it.frequency } ?: 1
        val normalizedFrequency = frequency / maxFrequency.toDouble()

        val score = (0.3 * preferenceScore) + (0.2 * staticPerkScore) +
                    (0.25 * historyScore) + (0.25 * normalizedFrequency)

        return score
    }

    override fun getTop3Categories(userId: Long): List<Long> {
        val allCategories = bankServiceProvider.getAllCategories()
        val categoryScores = allCategories.map { category ->
            category.id to calculateCategoryScore(userId, category.id)
        }
        return categoryScores
            .sortedByDescending { it.second }
            .take(3)
            .map { it.first }
    }
}