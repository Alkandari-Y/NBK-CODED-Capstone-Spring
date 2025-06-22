package com.project.recommendation.services

import com.project.common.data.requests.geofencing.GeoFenceEnterRequest
import com.project.common.data.responses.promotions.PromotionResponse
import com.project.recommendation.entities.PromotionEntity
import com.project.recommendation.entities.RecommendationEntity
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.repositories.PromotionRepository
import com.project.recommendation.repositories.RecommendationRepository
import java.time.LocalDate

class RecommendationServiceImpl(
    private val businessServiceProvider: BankServiceProvider,
    private val storeLocationsService: StoreLocationsService,
    private val promotionRepository: PromotionRepository,
    private val favBusinessService: FavBusinessService,
    private val favCategoriesService: FavCategoriesService,
    private val recommendationRepository: RecommendationRepository,
): RecommendationService {
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

        val promotions = if (favPartners.isNotEmpty()) {
            getPromotionByBusiness(favPartners.map { it.partnerId!! })
        } else {
            emptyList()
        }
        // if not zero get promotions

        val recommendation = RecommendationEntity(

        )
        // if no promotions get partner by category and user cards


        // if
        // get user fav categories

        // get
        return null
    }




    private fun getPromotionByBusiness(businessIds: List<Long>): List<PromotionEntity> {
        return promotionRepository.findActivePromotionsByBusinessPartnerId(businessIds, LocalDate.now())
    }


}