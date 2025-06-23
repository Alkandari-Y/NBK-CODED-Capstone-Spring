package com.project.recommendation.repositories

import com.project.recommendation.entities.CategoryScoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryScoreRepository : JpaRepository<CategoryScoreEntity, Long> {

    fun findAllByUserId(userId: Long): List<CategoryScoreEntity>

    fun findByCategoryIdAndUserId(categoryId: Long, userId: Long): CategoryScoreEntity?
}
