package com.project.recommendation.repositories

import com.project.recommendation.entities.PromotionCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PromotionCategoryRepository : JpaRepository<PromotionCategoryEntity, Long>
