package com.project.recommendation.services

import com.project.common.data.requests.geofencing.GeoFenceEnterRequest
import com.project.recommendation.entities.RecommendationEntity

interface RecommendationService {
    fun createGeofencingRecommendation(userId: Long, geofenceData: GeoFenceEnterRequest): RecommendationEntity?

}