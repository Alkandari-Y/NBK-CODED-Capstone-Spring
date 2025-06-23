package com.project.recommendation.services

import com.project.common.data.requests.geofencing.GeofenceEventRequest
import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.recommendation.entities.RecommendationEntity

interface RecommendationService {
    fun onboardingRecommendedCard(userId: Long): AccountProductDto
    fun createGeofencingRecommendation(geofenceData: GeofenceEventRequest): RecommendationEntity?

}