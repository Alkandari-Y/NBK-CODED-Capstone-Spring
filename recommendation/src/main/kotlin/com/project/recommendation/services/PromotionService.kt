package com.project.recommendation.services

import com.project.common.data.requests.promotions.PromotionRequest
import com.project.common.data.responses.promotions.PromotionResponse

interface PromotionService {
    fun createPromotion(request: PromotionRequest): PromotionResponse
    fun getPromotionById(id: Long): PromotionResponse?
    fun getAllPromotionsByBusinessId(businessId: Long): List<PromotionResponse>
}