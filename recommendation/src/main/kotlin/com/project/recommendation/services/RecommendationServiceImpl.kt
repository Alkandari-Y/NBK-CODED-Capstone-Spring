package com.project.recommendation.services

import com.project.common.data.requests.geofencing.GeoFenceEnterRequest
import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import com.project.common.data.responses.kyc.KYCResponse
import com.project.common.data.responses.promotions.PromotionResponse
import com.project.common.enums.AccountType
import com.project.common.enums.RecommendationType
import com.project.common.enums.RewardType
import com.project.common.exceptions.APIException
import com.project.common.exceptions.kyc.KycNotFoundException
import com.project.recommendation.entities.FavCategoryEntity
import com.project.recommendation.entities.PromotionEntity
import com.project.recommendation.entities.RecommendationEntity
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.PromotionRepository
import com.project.recommendation.repositories.RecommendationRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class RecommendationServiceImpl(
    private val businessServiceProvider: BankServiceProvider,
    private val storeLocationsService: StoreLocationsService,
    private val promotionRepository: PromotionRepository,
    private val favBusinessService: FavBusinessService,
    private val favCategoriesService: FavCategoriesService,
    private val recommendationRepository: RecommendationRepository,
): RecommendationService {
    override fun onboardingRecommendedCard(userId: Long): AccountProductDto {

        val userKyc = businessServiceProvider.getUserKyc(userId) ?: throw KycNotFoundException(userId)
        val businessPartners = businessServiceProvider.getAllBusinessPartners()
        val allProducts = businessServiceProvider.getAllAccountProducts()

        val favCategories = favCategoriesService.findAllFavCategories(userId)
        val favBusinesses = favBusinessService.findAllFavBusinesses(userId)


        val recommendedCard =  recommendCreditCards(
            userKyc = userKyc,
            favoriteCategories = favCategories,
            favoriteBusinesses = businessPartners.filter {
                it.id in favBusinesses.map { favBusiness -> favBusiness.partnerId }
             },
            allAccountProducts = allProducts.filter { it.accountType == AccountType.CREDIT.toString() },
        ).first()

        val recommendation = RecommendationEntity(
            genericIdRef = recommendedCard.id,
            userId= userId,
            recType = RecommendationType.ONBOARDING
        )
        recommendationRepository.save(recommendation)

        return recommendedCard.copy(recommended = true)
    }


    override fun createGeofencingRecommendation(
        userId: Long,
        geofenceData: GeoFenceEnterRequest
    ): RecommendationEntity? {
        // get userid or firebase token
        // call notification endpoint and check


        // get storelocations by gps
        val storeLocations = storeLocationsService.findNearbyStores(geofenceData)

        // get user fav partners
        val favPartners = favBusinessService.findAllFavBusinesses(userId)
        val favCategories = favCategoriesService.findAllFavCategories(userId)

        val promotions = if (favPartners.isNotEmpty()) {
            getPromotionByBusiness(favPartners.map { it.partnerId!! })
        } else {
            emptyList()
        }

//        val selectedPromotion = getUserBestPromotion(userId)

        // Placeholder for logic using the promotion
//        println("Selected promotion for user $userId: ${selectedPromotion?.id}")

        // if
        // get user fav categories and businesses

        // get
        val recommendation = RecommendationEntity(

        )
        // if no promotions get partner by category and user cards

        val selectedPromotion = getUserBestPromotion(userId)

        return null
    }




    private fun getPromotionByBusiness(businessIds: List<Long>): List<PromotionEntity> {
        return promotionRepository.findActivePromotionsByBusinessPartnerId(businessIds, LocalDate.now())
    }

    private fun recommendCreditCards(
        userKyc: KYCResponse,
        favoriteCategories: List<FavCategoryEntity>,
        favoriteBusinesses: List<BusinessPartnerDto>,
        allAccountProducts: List<AccountProductDto>,
    ): List<AccountProductDto> {

        val businessCategoryIds = favoriteBusinesses.map { it.category.id }.toSet()

        val eligibleCards = allAccountProducts.filter { card ->
            userKyc.salary >= card.minSalary
        }

        val maxXp = allAccountProducts
            .flatMap { it.perks }
            .maxOfOrNull { it.rewardsXp } ?: 1L
        val maxFee = allAccountProducts.maxOfOrNull { it.annualFee } ?: 0.0

        val rankedCards = eligibleCards.sortedByDescending { card ->
            var score = 0.0

            for (category in favoriteCategories) {
                if (category.categoryId in card.categoryIds) {
                    score += 10.0

                    for (perk in card.perks) {
                        perk.categories.forEach { perkCategory ->
                            if (perkCategory.id == category.categoryId) {
                                score += 50.0
                        }

                        if (perk.type == RewardType.CASHBACK) {
                            score += perk.perkAmount.toDouble()
                        }

                        if (perk.type == RewardType.DISCOUNT) {
                            score += perk.perkAmount.toDouble() * 75.0
                        }
                        }
                        score += perk.rewardsXp.toDouble() / maxXp * 100.0
                    }
                }
            }

            for (businessCategory in businessCategoryIds) {
                if (businessCategory in card.categoryIds) {
                    score += 7.0
                }

            }
            println("score: $score - product id: ${card.id}")

            score -= card.annualFee.toDouble() * 0.1

            score
        }

        return rankedCards.take(1)
    }

    private fun getUserBestPromotion(userId: Long): PromotionEntity? {
        val favBusinesses = favBusinessService.findAllFavBusinesses(userId)
        val businessIds = favBusinesses.mapNotNull { it.partnerId }

        if (businessIds.isEmpty()) return null

        val activePromotions = getPromotionByBusiness(businessIds)

        return activePromotions.maxByOrNull { it.endDate?.toEpochDay() ?: 0 }
    }

}