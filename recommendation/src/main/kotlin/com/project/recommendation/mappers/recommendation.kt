package com.project.recommendation.mappers

import com.project.common.enums.RecommendationType
import com.project.recommendation.entities.PromotionEntity
import com.project.recommendation.entities.RecommendationEntity
import java.time.LocalDateTime

fun PromotionEntity.toRecommendation(userId: Long): RecommendationEntity {
    return RecommendationEntity(
        userId = userId,
        genericIdRef = this.id!!,
        recType = RecommendationType.PROMOTION,
        createdAt = LocalDateTime.now()
    )
}