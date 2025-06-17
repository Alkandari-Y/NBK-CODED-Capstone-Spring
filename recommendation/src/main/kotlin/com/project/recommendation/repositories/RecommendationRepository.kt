package com.project.recommendation.repositories

import com.project.recommendation.entities.RecommendationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RecommendationRepository : JpaRepository<RecommendationEntity, Long>
