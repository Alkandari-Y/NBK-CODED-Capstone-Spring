package com.project.recommendation.mappers

import com.project.common.data.responses.RecommendationDto
import com.project.recommendation.entities.RecommendationEntity

fun RecommendationEntity.toDto() = RecommendationDto(
    id = id,
    genericIdRef = genericIdRef,
    userId = userId,
    recType = recType,
    createdAt = createdAt,
)