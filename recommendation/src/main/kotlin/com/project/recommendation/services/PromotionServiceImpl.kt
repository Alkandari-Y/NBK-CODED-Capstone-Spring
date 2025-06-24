package com.project.recommendation.services

import com.project.common.data.requests.promotions.CreatePromotionRequest
import com.project.common.data.responses.promotions.PromotionResponse
import com.project.common.exceptions.promotions.PromotionDateException
import com.project.common.exceptions.storeLocations.StoreLocationNotFoundException
import com.project.recommendation.entities.PromotionEntity
import com.project.recommendation.mappers.toEntity
import com.project.recommendation.mappers.toResponse
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.PromotionRepository
import com.project.recommendation.repositories.StoreLocationRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PromotionServiceImpl(
    private val promotionRepository: PromotionRepository,
    private val bankServiceProvider: BankServiceProvider,
    private val storeLocationRepository: StoreLocationRepository
) : PromotionService {
    override fun getAllPromotions(): List<PromotionResponse> {
        return promotionRepository.findAll().map { it.toResponse() }
    }

    @Transactional
    override fun createPromotion(request: CreatePromotionRequest): PromotionResponse {
        bankServiceProvider.getBusinessPartner(request.businessPartnerId)

        if (request.storeId != null) {
            storeLocationRepository.findByIdOrNull(request.storeId)
                ?: throw StoreLocationNotFoundException()
        }

        if (request.startDate != null && request.endDate != null
            && request.endDate!!.isBefore(request.startDate))
        { throw PromotionDateException() }

        val promotion = request.toEntity()

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

    override fun getPromotionForBusinesses(businessIds: List<Long>): List<PromotionEntity> {
        return promotionRepository.findActivePromotionsByBusinessPartnerIds(businessIds, LocalDate.now())
    }

    override fun getAllActivePromotionsByBusiness(businessId: Long): List<PromotionResponse> {
        return promotionRepository.findAllActivePromotionsByBusinessPartner(businessId, LocalDate.now()).map { it.toResponse() }
    }
}