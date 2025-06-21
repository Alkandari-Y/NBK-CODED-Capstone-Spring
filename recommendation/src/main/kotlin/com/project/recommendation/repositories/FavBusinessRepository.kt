package com.project.recommendation.repositories

import com.project.recommendation.entities.FavBusinessEntity
import com.project.recommendation.entities.FavCategoryEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FavBusinessRepository : JpaRepository<FavBusinessEntity, Long> {
    fun findAllByUserId(userId: Long): List<FavBusinessEntity>

    @Modifying
    @Transactional
    fun deleteAllByUserId(userId: Long)

    @Modifying
    @Transactional
    fun deleteByUserIdAndPartnerId(userId: Long, partnerId: Long)

    fun existsByUserIdAndPartnerId(userId: Long, partnerId: Long): Boolean

    @Modifying
    @Transactional
    @Query(
        "DELETE FROM FavBusinessEntity fb " +
                "WHERE fb.userId = :userId AND fb.partnerId IN :partnerIds"
    )
    fun deleteByUserIdAndPartnerIdIn(userId: Long, partnerIds: List<Long>)
}
