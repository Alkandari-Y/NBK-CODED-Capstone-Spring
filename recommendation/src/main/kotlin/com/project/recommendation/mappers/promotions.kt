package com.project.recommendation.mappers

import com.project.common.data.responses.promotions.PromotionResponse
import com.project.recommendation.entities.PromotionEntity

fun PromotionEntity.toResponse() = PromotionResponse(
    id = id,
    name = name,
    businessPartnerId = businessPartnerId,
    type = type,
    startDate = startDate,
    endDate = endDate,
    description = description,
    storeId = storeId,
    xp = xp
)