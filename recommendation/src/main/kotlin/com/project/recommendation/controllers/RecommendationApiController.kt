package com.project.recommendation.controllers

import com.project.common.data.requests.geofencing.GeoFenceEnterRequest
import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.common.data.responses.storeLocations.StoreLocationResponse
import com.project.common.security.RemoteUserPrincipal
import com.project.recommendation.entities.StoreLocationEntity
import com.project.recommendation.services.RecommendationService
import com.project.recommendation.services.StoreLocationsService
import jakarta.validation.Valid
import org.apache.http.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/recommendations")
class RecommendationApiController(
    private val storeLocationsService: StoreLocationsService,
    private val recommendationService: RecommendationService,
) {

    @GetMapping("/onBoarding")
    fun onboardingRecommendation(
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): AccountProductDto {
        return recommendationService.onboardingRecommendedCard(user.getUserId())
    }

    @PostMapping("/geofence") // triggers when user is within a geofenced area
    fun geofenceTrigger(
        @Valid @RequestBody geoFenceRequest: GeoFenceEnterRequest
    ): ResponseEntity<List<StoreLocationResponse>> {
        val storesWithinFence = storeLocationsService.findNearbyStores(geoFenceRequest)
        return ResponseEntity(storesWithinFence, HttpStatus.OK)
    }
}