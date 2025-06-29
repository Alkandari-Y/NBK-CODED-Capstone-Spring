package com.project.recommendation.services

import com.project.common.data.requests.accountProducts.AccountProductRecDto
import com.project.common.data.requests.ble.BlueToothBeaconNotificationRequest
import com.project.common.data.requests.geofencing.GeofenceEventRequest
import com.project.common.data.requests.notifications.BleBeaconNotificationDto
import com.project.common.data.requests.notifications.GeofenceNotificationDto
import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import com.project.common.data.responses.kyc.KYCResponse
import com.project.common.data.responses.perks.PerkDto
import com.project.common.enums.AccountType
import com.project.common.enums.ErrorCode
import com.project.common.enums.NotificationTriggerType
import com.project.common.enums.RecommendationType
import com.project.common.enums.RewardType
import com.project.common.exceptions.APIException
import com.project.common.exceptions.kyc.KycNotFoundException
import com.project.common.exceptions.storeLocations.StoreLocationNotFoundException
import com.project.recommendation.entities.FavCategoryEntity
import com.project.recommendation.entities.PromotionEntity
import com.project.recommendation.entities.RecommendationEntity
import com.project.recommendation.mappers.toRecommendation
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.providers.NotificationServiceProvider
import com.project.recommendation.repositories.PromotionRepository
import com.project.recommendation.repositories.RecommendationRepository
import com.project.recommendation.repositories.StoreLocationRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class RecommendationServiceImpl(
    private val businessServiceProvider: BankServiceProvider,
    private val notificationProvider: NotificationServiceProvider,
    private val storeLocationsService: StoreLocationsService,
    private val promotionService: PromotionService,
    private val favBusinessService: FavBusinessService,
    private val favCategoriesService: FavCategoriesService,
    private val recommendationRepository: RecommendationRepository,
    private val categoryScoreService: CategoryScoreService,
    private val promotionRepository: PromotionRepository,
    private val storeLocationRepository: StoreLocationRepository,
    private val bankServiceProvider: BankServiceProvider,
) : RecommendationService {
    private val logger = LoggerFactory.getLogger(GeofenceNotificationDto::class.java)

    override fun onboardingRecommendedCard(userId: Long): AccountProductDto {
        val userKyc = businessServiceProvider.getUserKyc(userId) ?: throw KycNotFoundException(userId)
        val businessPartners = businessServiceProvider.getAllBusinessPartners()
        val allProducts = businessServiceProvider.getAllAccountProducts()

        val favCategories = favCategoriesService.findAllFavCategories(userId)
        val favBusinesses = favBusinessService.findAllFavBusinesses(userId)

        val favoriteBusinessPartners = businessPartners.filter { partner ->
            partner.id in favBusinesses.map { it.partnerId }
        }

        val creditCards = allProducts.filter { it.accountType == AccountType.CREDIT.toString() }

        val recommendedCard = recommendCreditCards(
            userKyc = userKyc,
            favoriteCategories = favCategories,
            favoriteBusinesses = favoriteBusinessPartners,
            allAccountProducts = creditCards,
        ).firstOrNull() ?: throw APIException(
            "No eligible credit cards found for user: $userId",
            httpStatus = HttpStatus.BAD_REQUEST,
            code = ErrorCode.INVALID_INPUT
        )

        val recommendation = RecommendationEntity(
            genericIdRef = recommendedCard.id,
            userId = userId,
            recType = RecommendationType.ONBOARDING
        )
        recommendationRepository.save(recommendation)

        return recommendedCard.copy(recommended = true)
    }

    private fun recommendCreditCards(
        userKyc: KYCResponse,
        favoriteCategories: List<FavCategoryEntity>,
        favoriteBusinesses: List<BusinessPartnerDto>,
        allAccountProducts: List<AccountProductDto>,
    ): List<AccountProductDto> {

        val eligibleCards = allAccountProducts.filter { card ->
            userKyc.salary >= card.minSalary
        }

        if (eligibleCards.isEmpty()) {
            return emptyList()
        }

        val scoringMetrics = calculateScoringMetrics(eligibleCards)
        val favCategoryIds = favoriteCategories.map { it.categoryId }.toSet()
        val businessCategoryIds = favoriteBusinesses.map { it.category.id }.toSet()

        val rankedCards = eligibleCards.map { card ->
            val score = calculateCardScore(
                card = card,
                favCategoryIds = favCategoryIds,
                businessCategoryIds = businessCategoryIds,
                scoringMetrics = scoringMetrics,
                userSalary = userKyc.salary
            )

            CardScore(card, score)
        }.sortedByDescending { it.score }

        return rankedCards.map { it.card }
    }

    private fun calculateScoringMetrics(cards: List<AccountProductDto>): ScoringMetrics {
        val allPerks = cards.flatMap { it.perks }

        return ScoringMetrics(
            maxAnnualFee = cards.maxOfOrNull { it.annualFee } ?: BigDecimal.ZERO,
            maxXp = allPerks.maxOfOrNull { it.rewardsXp } ?: 1L,
            maxPerkAmount = allPerks.maxOfOrNull { it.perkAmount } ?: BigDecimal.ZERO,
            maxCreditLimit = cards.maxOfOrNull { it.creditLimit } ?: BigDecimal.ZERO
        )
    }

    private fun calculateCardScore(
        card: AccountProductDto,
        favCategoryIds: Set<Long?>,
        businessCategoryIds: Set<Long>,
        scoringMetrics: ScoringMetrics,
        userSalary: BigDecimal
    ): Double {
        var score = 0.0

        val categoryMatchWeight = 15.0
        val businessCategoryWeight = 25.0
        val cashbackMultiplier = 100.0
        val discountMultiplier = 75.0
        val xpWeight = 20.0
        val annualFeeWeight = 30.0
        val creditLimitWeight = 10.0

        score += scoreFavoriteCategories(
            card,
            favCategoryIds,
            categoryMatchWeight,
            cashbackMultiplier,
            discountMultiplier,
            xpWeight,
            scoringMetrics
        )
        score += scoreBusinessCategories(card, businessCategoryIds, businessCategoryWeight)
        score -= scoreAnnualFee(card, userSalary, annualFeeWeight)
        score += scoreCreditLimit(card, scoringMetrics, creditLimitWeight)
        score += scoreCardFeatures(card)
        return score
    }

    private fun scoreFavoriteCategories(
        card: AccountProductDto,
        favCategoryIds: Set<Long?>,
        categoryMatchWeight: Double,
        cashbackMultiplier: Double,
        discountMultiplier: Double,
        xpWeight: Double,
        scoringMetrics: ScoringMetrics
    ): Double {
        var categoryScore = 0.0

        val matchingCategories = card.categoryIds.intersect(favCategoryIds.filterNotNull().toSet())
        categoryScore += matchingCategories.size * categoryMatchWeight

        card.perks.forEach { perk ->
            val perkCategoryIds = perk.categories.map { it.id }.toSet()
            val matchingPerkCategories = perkCategoryIds.intersect(favCategoryIds.filterNotNull().toSet())

            if (matchingPerkCategories.isNotEmpty()) {
                categoryScore += matchingPerkCategories.size * 20.0
                when (perk.type) {
                    RewardType.CASHBACK -> {
                        categoryScore += perk.perkAmount.toDouble() * cashbackMultiplier
                    }

                    RewardType.DISCOUNT -> {
                        categoryScore += perk.perkAmount.toDouble() * discountMultiplier
                    }
                }

                if (scoringMetrics.maxXp > 0) {
                    categoryScore += (perk.rewardsXp.toDouble() / scoringMetrics.maxXp.toDouble()) * xpWeight
                }
            }
        }

        return categoryScore
    }

    private fun scoreBusinessCategories(
        card: AccountProductDto,
        businessCategoryIds: Set<Long>,
        businessCategoryWeight: Double
    ): Double {
        val matchingBusinessCategories = card.categoryIds.intersect(businessCategoryIds)
        return matchingBusinessCategories.size * businessCategoryWeight
    }

    private fun scoreAnnualFee(
        card: AccountProductDto,
        userSalary: BigDecimal,
        annualFeeWeight: Double
    ): Double {
        val feeToSalaryRatio = if (userSalary > BigDecimal.ZERO) {
            card.annualFee.divide(userSalary, 4, RoundingMode.HALF_UP).toDouble()
        } else {
            card.annualFee.toDouble()
        }

        return feeToSalaryRatio * annualFeeWeight * 1000
    }

    private fun scoreCreditLimit(
        card: AccountProductDto,
        scoringMetrics: ScoringMetrics,
        creditLimitWeight: Double
    ): Double {
        return if (scoringMetrics.maxCreditLimit > BigDecimal.ZERO) {
            (card.creditLimit.divide(scoringMetrics.maxCreditLimit, 4, RoundingMode.HALF_UP)
                .toDouble()) * creditLimitWeight
        } else {
            0.0
        }
    }

    private fun scoreCardFeatures(card: AccountProductDto): Double {
        var featureScore = 0.0

        featureScore += card.perks.size * 5.0
        featureScore += card.categoryIds.size * 3.0

        featureScore += maxOf(0.0, (30.0 - card.interestRate.toDouble())) * 2.0

        return featureScore
    }

    // TODO: gut like a fish and fix (the current impl uses onboarding-specific functions)
    // in CategoryScoreService there are two new functions
    // calculateCategoryScore(), and getTop3Categories()
    // you can use getTop3, it iterates the calculation over every single category
    // and returns the top 3, obviously
    // the function below needs to calculate the top 3 products
    // send num 1 to the account score drop notif
    //    (or make a different function for that one?)
    // send to another fun that lists all acc products
    //    - first the top 3, highlighted
    //    - then the rest, that are eligible
    //    - then the remaining ineligible ones
    override fun getTopProductRecommendations(userId: Long): List<AccountProductDto> {
//        val userKyc = businessServiceProvider.getUserKyc(userId) ?: throw KycNotFoundException(userId)
//        val businessPartners = businessServiceProvider.getAllBusinessPartners()
//        val allProducts = businessServiceProvider.getAllAccountProducts()
//
//        val favCategories = favCategoriesService.findAllFavCategories(userId)
//        val favBusinesses = favBusinessService.findAllFavBusinesses(userId)
//
//        val favoriteBusinessPartners = businessPartners.filter { partner ->
//            partner.id in favBusinesses.map { it.partnerId }
//        }
//
//        val creditCards = allProducts.filter { it.accountType == AccountType.CREDIT.toString() }
//
//        return recommendCreditCards(
//            userKyc = userKyc,
//            favoriteCategories = favCategories,
//            favoriteBusinesses = favoriteBusinessPartners,
//            allAccountProducts = creditCards,
//        ).take(3).map { it.copy(recommended = true) }
        return emptyList()
    }


    override fun createGeofencingRecommendation(
        geofenceData: GeofenceEventRequest,
    ): RecommendationEntity? {

        val userId = geofenceData.userId
        if (userId == 0L) {
            logger.warn("Geofence event received with invalid userId: $userId")
        }

        logger.info("Processing geofence event for userId: $userId at location: ${geofenceData.name}")

        val nearbyStores = storeLocationsService.findNearbyStores(geofenceData)
        if (nearbyStores.isEmpty()) {
            logger.info("No nearby stores found for userId: $userId")
        }

        val nearbyPartnerIds = nearbyStores.mapNotNull { it.partnerId }.toSet()
        logger.info("Found ${nearbyPartnerIds.size} nearby partners for userId: $userId")

        val allPartners = businessServiceProvider.getAllBusinessPartners()

        val favPartnerIds = favBusinessService.findAllFavBusinesses(userId).mapNotNull { it.partnerId }.toSet()
        val favCategories = favCategoriesService.findAllFavCategories(userId).mapNotNull { it.categoryId }.toSet()
        val categoryScores = categoryScoreService.findAllCategoryScores(userId)

        val topScoredCategoryIds = categoryScores?.sortedByDescending { it.frequency }
            ?.take(3)
            ?.mapNotNull { it.categoryId }
            ?.toSet()

        logger.info("User $userId has ${favPartnerIds.size} favorite partners and ${favCategories.size} favorite categories")
        logger.info("Top scored categories for user $userId: $topScoredCategoryIds")

        val favNearbyBusinessIds = favPartnerIds.intersect(nearbyPartnerIds).toList()
        logger.info("Found ${favNearbyBusinessIds.size} nearby favorite businesses for userId: $userId")

        val nearbyFavPromotions = promotionService.getPromotionForBusinesses(favNearbyBusinessIds)
            .filter { isPromotionActive(it) }

        if (nearbyFavPromotions.isNotEmpty()) {
            logger.info("Found ${nearbyFavPromotions.size} active promotions for favorite nearby businesses")

            val bestPromotion = nearbyFavPromotions.maxByOrNull { it.endDate?.toEpochDay() ?: 0 } ?: return null
            val recommendation = recommendationRepository.save(bestPromotion.toRecommendation(userId))
            val partner = allPartners.firstOrNull { it.id == bestPromotion.businessPartnerId } ?: return null

            logger.info("Sending promotion notification from favorite nearby business: ${partner.name}")
            sendGeoNotification(
                promotion = bestPromotion,
                recommendation = recommendation,
                partner = partner,
                geofenceData = geofenceData
            )
            return recommendation
        }

        val categoryMatchedPartners = allPartners.filter {
            it.category.id in favCategories || topScoredCategoryIds?.contains(it.category.id) == true
        }

        logger.info("Found ${categoryMatchedPartners.size} partners matching favorite or top scored categories")

        val partnerIdsByCategory = categoryMatchedPartners.map { it.id }
        val categoryBasedPromotions = promotionService.getPromotionForBusinesses(partnerIdsByCategory)
            .filter { isPromotionActive(it) }

        if (categoryBasedPromotions.isNotEmpty()) {
            logger.info("Found ${categoryBasedPromotions.size} active promotions for category-matched partners")

            val bestPromotion = categoryBasedPromotions.maxByOrNull { it.endDate?.toEpochDay() ?: 0 } ?: return null
            val recommendation = recommendationRepository.save(bestPromotion.toRecommendation(userId))
            val partner = allPartners.firstOrNull { it.id == bestPromotion.businessPartnerId } ?: return null
            sendGeoNotification(
                promotion = bestPromotion,
                recommendation = recommendation,
                partner = partner,
                geofenceData = geofenceData
            )

            logger.info("Sending promotion notification from category-matched partner: ${partner.name}")
            sendGeoNotification(promotion = bestPromotion, recommendation = recommendation, partner = partner, geofenceData = geofenceData)
            return recommendation
        }

        logger.warn("No personalized promotions found. Sending fallback notification.")

        val fallbackPartners = businessServiceProvider.getAllBusinessPartners()
        val anyPromotion = promotionRepository.findAll().firstOrNull()

        if (anyPromotion != null) {
            val recommendation = recommendationRepository.save(anyPromotion.toRecommendation(userId))
            val partner = fallbackPartners.find { it.id == anyPromotion.businessPartnerId } ?: return null

            logger.info("Sending fallback promotion from partner: ${partner.name}")
            sendGeoNotification(
                promotion = anyPromotion,
                recommendation = recommendation,
                partner = partner,
                geofenceData = geofenceData
            )
        } else {
            logger.error("No fallback promotions available to send")
        }

        return null
    }

    override fun triggerAccountScoreNotif(request: AccountProductRecDto) {
        // TODO: impl for triggering the account score notif, with the top card recommendation
    }

    override fun triggerBluetoothBeaconNotification(
        request: BlueToothBeaconNotificationRequest
    ) {
        logger.info("Received bluetooth beacon notification for userId: ${request.userId} and beaconId: ${request.beaconId}")

        val storeLocation = storeLocationRepository.findByBeaconId(request.beaconId)
            ?: throw StoreLocationNotFoundException()
        logger.info("Found store location: $storeLocation for beaconId: ${request.beaconId}")

        val response = bankServiceProvider.getUserBleRecommendationDataInput(
            userId = request.userId,
            businessPartnerId = storeLocation.partnerId
                ?: throw APIException("No partnerId found for store location: $storeLocation", HttpStatus.BAD_REQUEST)
        )
        logger.info("Received BLE recommendation data for userId: ${request.userId}, partnerId: ${storeLocation.partnerId}")

        val userUniqueAccountProductIds = response.userData.uniqueUserProducts
        val businessPartner = response.relatedPartnerSummary

        if (storeLocation.partnerId != businessPartner.id) {
            throw APIException(
                "PartnerId mismatch for store location: $storeLocation with businessPartner: $businessPartner",
                HttpStatus.BAD_REQUEST
            )
        }
        logger.info("PartnerId validation passed for storeLocation: ${storeLocation.id} and businessPartner: ${businessPartner.id}")

        val allAccountProducts = bankServiceProvider.getAllAccountProducts()
        logger.info("Retrieved ${allAccountProducts.size} total account products")

        val partnerPromotions = promotionRepository.findAllActivePromotionsByBusinessPartner(
            businessId = businessPartner.id,
            currentDate = LocalDate.now(),
        )
        logger.info("Found ${partnerPromotions.size} active promotions for businessPartnerId: ${businessPartner.id}")

        if (partnerPromotions.isNotEmpty()) {
            val promotion = partnerPromotions.find { it.storeId == businessPartner.id } ?: partnerPromotions.first()
            logger.info("Triggering BLE notification for promotionId: ${promotion.id} for userId: ${request.userId}")

            val recommendation = RecommendationEntity(
                genericIdRef = promotion.id!!,
                userId = request.userId,
                recType = RecommendationType.PROMOTION
            )

            notificationProvider.sendBledNotification(
                notification = BleBeaconNotificationDto(
                    userId = request.userId,
                    message = "${businessPartner.name} has a promotion nearby.",
                    partnerId = businessPartner.id,
                    recommendationId = recommendation.id,
                    promotionId = promotion.id,
                    triggerType = NotificationTriggerType.BEACON,
                )
            )
            logger.info("Sent BLE promotion notification for userId: ${request.userId} with promotionId: ${promotion.id}")
        } else {
            logger.info("No active promotions found; attempting account product BLE recommendation")
            val filteredRelatedAccountProducts = response.allProducts.filter { product ->
                product.categoryIds.contains(businessPartner.categoryId)
            }
            logger.info("Filtered to ${filteredRelatedAccountProducts.size} related account products matching business category")

            if (filteredRelatedAccountProducts.isEmpty()) {
                logger.info("No user products match the business category; skipping BLE recommendation")
                return
            }

            val bestUserCard = findBestUserCardForBusinessCategory(
                userOwnedAccountProducts = userUniqueAccountProductIds,
                allAccountProducts = allAccountProducts,
                businessCategoryId = businessPartner.categoryId
            )

            if (bestUserCard != null) {
                logger.info("Best user card determined: id=${bestUserCard.id}, name=${bestUserCard.name}")

                val recommendation = RecommendationEntity(
                    genericIdRef = bestUserCard.id!!,
                    userId = request.userId,
                    recType = RecommendationType.ACCOUNT_PRODUCT
                )
                notificationProvider.sendBledNotification(
                    notification = BleBeaconNotificationDto(
                        userId = request.userId,
                        message = "Use your ${bestUserCard.name} to earn cashback or discounts at ${businessPartner.name}.",
                        partnerId = businessPartner.id,
                        recommendationId = recommendation.id,
                        promotionId = null,
                        triggerType = NotificationTriggerType.BEACON,
                    )
                )
                logger.info("Sent BLE account product notification for userId: ${request.userId} with accountProductId: ${bestUserCard.id}")
            } else {
                logger.info("No suitable user card found for BLE recommendation at ${businessPartner.name}")
            }
        }
    }


    private fun findBestUserCardForBusinessCategory(
        userOwnedAccountProducts: List<Long>,
        allAccountProducts: List<AccountProductDto>,
        businessCategoryId: Long
    ): AccountProductDto? {
        logger.debug("Finding best user card for businessCategoryId: $businessCategoryId with userOwnedProductIds: $userOwnedAccountProducts")

        val userProducts = allAccountProducts.filter { it.id != null && userOwnedAccountProducts.contains(it.id) }
        logger.debug("Filtered ${userProducts.size} user-owned products from allAccountProducts")

        val relevantProducts = userProducts.filter { it.categoryIds.contains(businessCategoryId) }
        logger.debug("Filtered ${relevantProducts.size} relevant products matching business category $businessCategoryId")

        if (relevantProducts.isEmpty()) {
            logger.debug("No relevant user-owned account products found for business category $businessCategoryId")
            return null
        }

        val prioritizedProduct = relevantProducts
            .map { product ->
                val cashbackPerks = product.perks.filter {
                    it.type == RewardType.CASHBACK &&
                            it.categories.any { category -> category.id == businessCategoryId }
                }
                val discountPerks = product.perks.filter {
                    it.type == RewardType.DISCOUNT &&
                            it.categories.any { category -> category.id == businessCategoryId }
                }

                val highestCashback = cashbackPerks.maxByOrNull { it.perkAmount }
                val highestDiscount = discountPerks.maxByOrNull { it.perkAmount }

                logger.debug("Product id=${product.id} name=${product.name} has cashbackPerks=${cashbackPerks.size}, discountPerks=${discountPerks.size}, highestCashback=${highestCashback?.perkAmount}, highestDiscount=${highestDiscount?.perkAmount}")

                Triple(product, highestCashback, highestDiscount)
            }
            .sortedWith(
                compareByDescending<Triple<AccountProductDto, PerkDto?, PerkDto?>> { it.second != null }
                    .thenByDescending { it.second?.perkAmount ?: BigDecimal.ZERO }
                    .thenByDescending { it.third != null }
                    .thenByDescending { it.third?.perkAmount ?: BigDecimal.ZERO }
            )
            .firstOrNull()

        prioritizedProduct?.let {
            logger.debug("Selected product id=${it.first.id}, name=${it.first.name} for BLE notification")
        } ?: logger.debug("No prioritized product found for BLE recommendation")

        return prioritizedProduct?.first
    }


    private fun sendGeoNotification(
        promotion: PromotionEntity,
        recommendation: RecommendationEntity,
        partner: BusinessPartnerDto,
        geofenceData: GeofenceEventRequest
    ) {

        val intro = "${partner.name} has a promotion nearby" ?: "Enjoy promotions"
        logger.info(intro)
        notificationProvider.sendGeoFencedNotification(
            GeofenceNotificationDto(
                userId = recommendation.userId!!,
                message = "$intro at ${geofenceData.name}",
                partnerId = promotion.businessPartnerId,
                eventId = null,
                recommendationId = recommendation.id!!,
                promotionId = promotion.id!!,
                triggerType = NotificationTriggerType.GPS,
                geofenceEventRequest = geofenceData
            )
        )
    }

    private fun isPromotionActive(promotion: PromotionEntity): Boolean {
        val today = LocalDate.now()
        return (promotion.startDate == null || !promotion.startDate!!.isAfter(today)) &&
                (promotion.endDate == null || !promotion.endDate!!.isBefore(today))
    }
}


// For Internal uses within the service

private data class CardScore(
    val card: AccountProductDto,
    val score: Double
)

private data class ScoringMetrics(
    val maxAnnualFee: BigDecimal,
    val maxXp: Long,
    val maxPerkAmount: BigDecimal,
    val maxCreditLimit: BigDecimal
)