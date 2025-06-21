package com.project.recommendation.services

import com.project.common.data.requests.categories.FavCategoriesRequest
import com.project.common.data.requests.categories.FavoriteCategoryRequest
import com.project.recommendation.entities.FavCategoryEntity



interface FavCategoriesService {
    fun findAllFavCategories(userId: Long): List<FavCategoryEntity>
    fun setAllFavCategories(favCategoriesRequest: FavCategoriesRequest, userId: Long): List<FavCategoryEntity>
    fun addOneFavCategory(favoriteCategoryRequest: FavoriteCategoryRequest, userId: Long): FavCategoryEntity
    fun removeFavCategories(userId: Long, favCategoriesRequest: FavCategoriesRequest)
    fun removeOneFavCategory(userId: Long, favoriteCategoryRequest: FavoriteCategoryRequest)
    fun clearAllFavCategories(userId: Long)
}