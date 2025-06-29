package com.project.recommendation.mappers

import com.project.common.data.requests.promotions.CreatePromotionRequest
import com.project.common.data.responses.promotions.PromotionResponse
import com.project.recommendation.entities.PromotionEntity


fun CreatePromotionRequest.toEntity() = PromotionEntity(
    name = name,
    businessPartnerId = businessPartnerId,
    startDate = startDate,
    endDate = endDate,
    description = description,
    storeId = storeId
)

fun PromotionEntity.toResponse() = PromotionResponse(
    id = id!!,
    name = name,
    businessPartnerId = businessPartnerId!!,
    type = type!!,
    startDate = startDate!!,
    endDate = endDate!!,
    description = description,
    storeId = storeId
)