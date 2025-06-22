package com.project.recommendation.services

import com.project.common.data.requests.categories.FavCategoriesRequest
import com.project.common.data.requests.categories.FavoriteCategoryRequest
import com.project.common.exceptions.favorites.FavCategoriesLimitException
import com.project.common.exceptions.favorites.FavoriteCategoryExistsForUserException
import com.project.recommendation.entities.FavCategoryEntity
import com.project.recommendation.mappers.toEntityList
import com.project.recommendation.repositories.FavCategoryRepository
import org.springframework.stereotype.Service

@Service
class FavCategoriesServiceImpl(
    private val favCategoryRepository: FavCategoryRepository
) : FavCategoriesService {
    private val MAX_CATEGORIES = 5

    override fun findAllFavCategories(userId: Long): List<FavCategoryEntity> {
        return favCategoryRepository.findAllByUserId(userId)
    }

    override fun setAllFavCategories(
        favCategoriesRequest: FavCategoriesRequest,
        userId: Long
    ): List<FavCategoryEntity> {
        if (favCategoriesRequest.categoryIds.size >= MAX_CATEGORIES) {
            throw FavCategoriesLimitException()
        }

        favCategoryRepository.deleteAllByUserId(userId)

        return favCategoryRepository.saveAll(
            favCategoriesRequest.toEntityList(userId)
        )
    }

    override fun addOneFavCategory(
        favoriteCategoryRequest: FavoriteCategoryRequest,
        userId: Long
    ): FavCategoryEntity {
        val currentFavs = favCategoryRepository.findAllByUserId(userId)
        if (currentFavs.size >= MAX_CATEGORIES) { throw FavCategoriesLimitException() }

        if (favCategoryRepository.existsByUserIdAndCategoryId(userId, favoriteCategoryRequest.categoryId)) {
            throw FavoriteCategoryExistsForUserException()
        }

        val fav = FavCategoryEntity(
            userId = userId,
            categoryId = favoriteCategoryRequest.categoryId
        )
        return favCategoryRepository.save(fav)
    }

    override fun removeFavCategories(userId: Long, favCategoriesRequest: FavCategoriesRequest) {
        favCategoryRepository.deleteByUserIdAndCategoryIdIn(userId, favCategoriesRequest.categoryIds)
    }

    override fun removeOneFavCategory(userId: Long, favoriteCategoryRequest: FavoriteCategoryRequest) {
        favCategoryRepository.deleteByUserIdAndCategoryId(userId, favoriteCategoryRequest.categoryId)
    }

    override fun clearAllFavCategories(userId: Long) {
        favCategoryRepository.deleteAllByUserId(userId)
    }
}
