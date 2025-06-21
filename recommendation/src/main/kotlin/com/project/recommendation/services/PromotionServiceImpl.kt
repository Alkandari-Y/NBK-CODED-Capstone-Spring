package com.project.recommendation.services

import com.google.shopping.type.Price
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.common.data.requests.promotions.CreatePromotionRequest
import com.project.common.data.responses.promotions.PromotionResponse
import com.project.common.exceptions.businessPartner.BusinessNotFoundException
import com.project.recommendation.entities.PromotionEntity
import com.project.recommendation.mappers.toEntity
import com.project.recommendation.mappers.toResponse
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.PromotionRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PromotionServiceImpl(
    private val promotionRepository: PromotionRepository,
    private val bankServiceProvider: BankServiceProvider
) : PromotionService {
    @Transactional
    override fun createPromotion(request: CreatePromotionRequest): PromotionResponse {
        val businessPartner = bankServiceProvider.getBusinessPartner(request.businessPartnerId)

        val promotion = request.toEntity(businessPartner.id)

        return promotionRepository.save(promotion).toResponse()
    }

    override fun getPromotionById(id: Long): PromotionResponse? {
        return promotionRepository.findByIdOrNull(id)?.toResponse()
    }

    override fun getAllPromotionsByBusinessId(businessId: Long): List<PromotionResponse> {
        val businessPartner = bankServiceProvider.getBusinessPartner(businessId)
        return promotionRepository.findAllByBusinessPartnerId(businessPartner.id)
            .map { it.toResponse() }
    }
}