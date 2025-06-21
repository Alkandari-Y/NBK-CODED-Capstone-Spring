package com.project.recommendation.services

import com.project.banking.repositories.BusinessPartnerRepository
import com.project.common.data.requests.promotions.CreatePromotionRequest
import com.project.common.data.responses.promotions.PromotionResponse
import com.project.common.exceptions.businessPartner.BusinessNotFoundException
import com.project.recommendation.entities.PromotionEntity
import com.project.recommendation.mappers.toResponse
import com.project.recommendation.repositories.PromotionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PromotionServiceImpl(
    private val promotionRepository: PromotionRepository,
//    private val businessPartnerRepository: BusinessPartnerRepository
) : PromotionService {
    override fun createPromotion(request: CreatePromotionRequest): PromotionResponse {
//        val businessPartner = businessPartnerRepository.findByIdOrNull(request.businessPartnerId)
//            ?: throw BusinessNotFoundException()

        val promotion = PromotionEntity(
            name = request.name,
            businessPartnerId = request.businessPartnerId,
            type = request.type,
            startDate = request.startDate,
            endDate = request.endDate,
            description = request.description,
            storeId = request.storeId,
            xp = request.xp
        )
        return promotionRepository.save(promotion).toResponse()
    }

    override fun getPromotionById(id: Long): PromotionResponse? {
        return promotionRepository.findByIdOrNull(id)?.toResponse()
    }

    override fun getAllPromotionsByBusinessId(businessId: Long): List<PromotionResponse> {
//        val businessPartner = businessPartnerRepository.findByIdOrNull(businessId)
//            ?: throw BusinessNotFoundException()

        return promotionRepository.findAllByBusinessPartnerId(businessId)
            .map { it.toResponse() }
    }
}