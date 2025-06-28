package com.project.recommendation.services

import com.project.common.data.requests.accountProducts.AccountProductRecDto
import com.project.common.data.requests.ble.BlueToothBeaconNotificationRequest
import com.project.common.data.requests.geofencing.GeofenceEventRequest
import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.recommendation.entities.RecommendationEntity

interface RecommendationService {
    fun onboardingRecommendedCard(userId: Long): AccountProductDto
    fun createGeofencingRecommendation(geofenceData: GeofenceEventRequest): RecommendationEntity?
    fun triggerAccountScoreNotif(request: AccountProductRecDto)
    fun getTopProductRecommendations(userId: Long): List<AccountProductDto>
    fun triggerBluetoothBeaconNotification(request: BlueToothBeaconNotificationRequest)
}