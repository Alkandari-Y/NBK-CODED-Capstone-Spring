package com.project.recommendation.repositories

import com.project.recommendation.entities.PromotionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface PromotionRepository : JpaRepository<PromotionEntity, Long>{
    fun findAllByBusinessPartnerId(businessId: Long): List<PromotionEntity>


    @Query("""
    SELECT p FROM PromotionEntity p 
        WHERE p.businessPartnerId = :businessId
            AND :currentDate BETWEEN p.startDate AND p.endDate
""")
    fun findAllActivePromotionsByBusinessPartner(
        @Param("businessId") businessId: Long,
        @Param("currentDate") currentDate: LocalDate
    ): List<PromotionEntity>


    @Query("""
    SELECT p FROM PromotionEntity p 
        WHERE p.businessPartnerId in :businessIds 
            AND :currentDate BETWEEN p.startDate AND p.endDate
""")
    fun findActivePromotionsByBusinessPartnerId(
        @Param("businessIds") businessIds: List<Long>,
        @Param("currentDate") currentDate: LocalDate
    ): List<PromotionEntity>
}
