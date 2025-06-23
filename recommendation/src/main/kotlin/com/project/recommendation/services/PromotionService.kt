package com.project.recommendation.services

import com.project.common.data.requests.promotions.CreatePromotionRequest
import com.project.common.data.responses.promotions.PromotionResponse
import com.project.recommendation.entities.PromotionEntity

interface PromotionService {
    fun getAllPromotions(): List<PromotionResponse>
    fun createPromotion(request: CreatePromotionRequest): PromotionResponse
    fun getPromotionById(id: Long): PromotionResponse?
    fun getAllPromotionsByBusinessId(businessId: Long): List<PromotionResponse>
    fun getPromotionForBusinesses(businessIds: List<Long>): List<PromotionEntity>
    fun getAllActivePromotionsByBusiness(businessId: Long): List<PromotionResponse>
}