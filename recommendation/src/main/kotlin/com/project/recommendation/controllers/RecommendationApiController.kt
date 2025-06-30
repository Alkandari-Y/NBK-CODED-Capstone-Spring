package com.project.recommendation.controllers

import com.project.common.data.requests.accountProducts.AccountProductRecDto
import com.project.common.data.requests.ble.BlueToothBeaconNotificationRequest
import com.project.common.data.requests.geofencing.GeofenceEventRequest
import com.project.common.data.requests.recommendations.RecommendedAccountProducts
import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.common.security.RemoteUserPrincipal
import com.project.recommendation.services.RecommendationService
import com.project.recommendation.services.StoreLocationsService
import jakarta.validation.Valid
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
    @GetMapping
    fun getTopProductRecommendations(
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): ResponseEntity<List<RecommendedAccountProducts>> {
        val recommended = recommendationService.getTopProductRecommendations(user.getUserId())
        return ResponseEntity.ok(recommended)
    }

    @PostMapping("/account-score")
    fun accountScoreRecommendationNotificationTrigger(@Valid @RequestBody request: AccountProductRecDto): ResponseEntity<Void> {
        recommendationService.triggerAccountScoreNotif(request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/onBoarding")
    fun onboardingRecommendationTrigger(
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): AccountProductDto {
        return recommendationService.onboardingRecommendedCard(user.getUserId())
    }

    @PostMapping("/geofence")
    fun geofenceNotificationTrigger(
        @Valid @RequestBody geoFenceRequest: GeofenceEventRequest
    ): ResponseEntity<Unit> {
        recommendationService.createGeofencingRecommendation(geoFenceRequest)
         return ResponseEntity.ok().build()
    }

    @PostMapping("/bluetooth-beacon")
    fun bluetoothBeaconNotificationTrigger(
        @Valid @RequestBody request: BlueToothBeaconNotificationRequest
    ): ResponseEntity<Unit> {
        recommendationService.triggerBluetoothBeaconNotification(request)
        return ResponseEntity.ok().build()
    }
}