package com.project.recommendation.mappers

import com.project.common.data.requests.categories.FavCategoriesRequest
import com.project.common.data.responses.categories.FavoriteCategoriesResponse
import com.project.common.data.responses.categories.FavoriteCategoryDto
import com.project.recommendation.entities.FavCategoryEntity
import java.time.LocalDateTime

fun List<FavCategoryEntity>.toFavoriteCategoryResponse(): FavoriteCategoriesResponse {
    return FavoriteCategoriesResponse(this.map {
        FavoriteCategoryDto(
            id = it.id!!,
            categoryId = it.categoryId!!,
            createAt = it.createdAt
        )
    })
}

fun FavCategoriesRequest.toEntityList(userId: Long): List<FavCategoryEntity> {
    return this.categoryIds.map {
        FavCategoryEntity(userId = userId, categoryId = it
        )
    }
}

