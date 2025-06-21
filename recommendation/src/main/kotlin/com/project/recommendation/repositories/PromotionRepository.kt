package com.project.recommendation.repositories

import com.project.recommendation.entities.PromotionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PromotionRepository : JpaRepository<PromotionEntity, Long>{
    fun findAllByBusinessPartnerId(businessId: Long): List<PromotionEntity>
}
