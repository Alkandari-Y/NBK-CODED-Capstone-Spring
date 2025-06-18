package com.project.recommendation.repositories

import com.project.recommendation.entities.FavCategoryEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FavCategoryRepository : JpaRepository<FavCategoryEntity, Long> {

    fun findAllByUserId(userId: Long): List<FavCategoryEntity>

    @Modifying
    @Transactional
    fun deleteAllByUserId(userId: Long)

    @Modifying
    @Transactional
    fun deleteByUserIdAndCategoryId(userId: Long, categoryId: Long)

    fun existsByUserIdAndCategoryId(userId: Long, categoryId: Long): Boolean

    @Modifying
    @Transactional
    @Query(
        "DELETE FROM FavCategoryEntity f " +
                "WHERE f.userId = :userId AND f.categoryId IN :categoryIds"
    )
    fun deleteByUserIdAndCategoryIdIn(userId: Long, categoryIds: List<Long>)
}
