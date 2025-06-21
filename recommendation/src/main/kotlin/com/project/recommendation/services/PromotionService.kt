package com.project.recommendation.services

import com.project.common.data.requests.promotions.CreatePromotionRequest
import com.project.common.data.responses.promotions.PromotionResponse

interface PromotionService {
    fun createPromotion(request: CreatePromotionRequest): PromotionResponse
    fun getPromotionById(id: Long): PromotionResponse?
    fun getAllPromotionsByBusinessId(businessId: Long): List<PromotionResponse>
}