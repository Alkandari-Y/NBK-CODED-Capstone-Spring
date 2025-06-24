package com.project.recommendation.services

import com.project.common.data.requests.accountProducts.AccountProductRecDto
import com.project.common.data.requests.geofencing.GeofenceEventRequest
import com.project.common.data.requests.notifications.NotificationDto
import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import com.project.common.data.responses.kyc.KYCResponse
import com.project.common.enums.AccountType
import com.project.common.enums.ErrorCode
import com.project.common.enums.NotificationTriggerType
import com.project.common.enums.RecommendationType
import com.project.common.enums.RewardType
import com.project.common.exceptions.APIException
import com.project.common.exceptions.kyc.KycNotFoundException
import com.project.recommendation.entities.FavCategoryEntity
import com.project.recommendation.entities.PromotionEntity
import com.project.recommendation.entities.RecommendationEntity
import com.project.recommendation.mappers.toRecommendation
import com.project.recommendation.providers.BankServiceProvider
import com.project.recommendation.providers.NotificationProvider
import com.project.recommendation.repositories.RecommendationRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class RecommendationServiceImpl(
    private val businessServiceProvider: BankServiceProvider,
    private val notificationProvider: NotificationProvider,
    private val storeLocationsService: StoreLocationsService,
    private val promotionService: PromotionService,
    private val favBusinessService: FavBusinessService,
    private val favCategoriesService: FavCategoriesService,
    private val recommendationRepository: RecommendationRepository,
    private val categoryScoreService: CategoryScoreService,
) : RecommendationService {
    private val logger = LoggerFactory.getLogger(NotificationDto::class.java)

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

    fun getTopProductRecommendations(
        userId: Long,
        limit: Int = 3
    ): List<AccountProductDto> {
        val userKyc = businessServiceProvider.getUserKyc(userId) ?: throw KycNotFoundException(userId)
        val businessPartners = businessServiceProvider.getAllBusinessPartners()
        val allProducts = businessServiceProvider.getAllAccountProducts()

        val favCategories = favCategoriesService.findAllFavCategories(userId)
        val favBusinesses = favBusinessService.findAllFavBusinesses(userId)

        val favoriteBusinessPartners = businessPartners.filter { partner ->
            partner.id in favBusinesses.map { it.partnerId }
        }

        val creditCards = allProducts.filter { it.accountType == AccountType.CREDIT.toString() }

        return recommendCreditCards(
            userKyc = userKyc,
            favoriteCategories = favCategories,
            favoriteBusinesses = favoriteBusinessPartners,
            allAccountProducts = creditCards,
        ).take(limit).map { it.copy(recommended = true) }
    }


    override fun createGeofencingRecommendation(
        geofenceData: GeofenceEventRequest,
    ): RecommendationEntity? {

        val userId = geofenceData.userId
        if (userId == 0L) return null

        val nearbyStores = storeLocationsService.findNearbyStores(geofenceData)
        if (nearbyStores.isEmpty()) return null

        val nearbyPartnerIds = nearbyStores.mapNotNull { it.partnerId }.toSet()
        val allPartners = businessServiceProvider.getAllBusinessPartners()

        val favPartnerIds = favBusinessService.findAllFavBusinesses(userId).mapNotNull { it.partnerId }.toSet()
        val favCategories = favCategoriesService.findAllFavCategories(userId).mapNotNull { it.categoryId }.toSet()
        val categoryScores = categoryScoreService.findAllCategoryScores(userId)
        val topScoredCategoryIds = categoryScores?.sortedByDescending { it.frequency }
            ?.take(3)
            ?.mapNotNull { it.categoryId }
            ?.toSet()

        val favNearbyBusinessIds = favPartnerIds.intersect(nearbyPartnerIds).toList()
        val nearbyFavPromotions = promotionService.getPromotionForBusinesses(favNearbyBusinessIds)
            .filter { isPromotionActive(it) }

        if (nearbyFavPromotions.isNotEmpty()) {
            val bestPromotion = nearbyFavPromotions.maxByOrNull { it.endDate?.toEpochDay() ?: 0 } ?: return null
            val recommendation = recommendationRepository.save(bestPromotion.toRecommendation(userId))
            val partner = allPartners.firstOrNull { it.id == bestPromotion.businessPartnerId } ?: return null
            sendGeoNotification(
                promotion = bestPromotion,
                recommendation = recommendation,
                partner = partner,
                geofenceData = geofenceData
            )

        }

        val categoryMatchedPartners = allPartners.filter {
            it.category.id in favCategories || topScoredCategoryIds?.contains(it.category.id) == true
        }

        val partnerIdsByCategory = categoryMatchedPartners.map { it.id }
        val categoryBasedPromotions = promotionService.getPromotionForBusinesses(partnerIdsByCategory)
            .filter { isPromotionActive(it) }

        if (categoryBasedPromotions.isNotEmpty()) {
            val bestPromotion = categoryBasedPromotions.maxByOrNull { it.endDate?.toEpochDay() ?: 0 } ?: return null
            val recommendation = recommendationRepository.save(bestPromotion.toRecommendation(userId))
            val partner = allPartners.firstOrNull { it.id == bestPromotion.businessPartnerId } ?: return null
            sendGeoNotification(
                promotion = bestPromotion,
                recommendation = recommendation,
                partner = partner,
                geofenceData = geofenceData
            )
        }

        return null
    }

    override fun triggerAccountScoreNotif(request: AccountProductRecDto) {
        // TODO Handled later — right now just receives data
    }

    private fun sendGeoNotification(
        promotion: PromotionEntity,
        recommendation: RecommendationEntity,
        partner: BusinessPartnerDto,
        geofenceData: GeofenceEventRequest
    ) {

        val intro = "${partner.name} has a promotion nearby" ?: "Enjoy promotions"
        logger.info(intro)
        notificationProvider.sendNotification(
            NotificationDto(
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