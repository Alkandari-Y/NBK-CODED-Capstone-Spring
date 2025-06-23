package com.project.recommendation.controllers

import com.project.common.data.requests.geofencing.GeoFenceEnterRequest
import com.project.common.data.requests.storeLocations.StoreLocationCreateRequest
import com.project.common.data.responses.storeLocations.StoreLocationResponse
import com.project.recommendation.mappers.toResponse
import com.project.recommendation.repositories.StoreLocationRepository
import com.project.recommendation.services.StoreLocationsService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/store-locations")
class StoreLocationsApiController(
    private val storeLocationsService: StoreLocationsService,
    private val storeLocationRepository: StoreLocationRepository,
) {
    @GetMapping
    fun getAllStoreLocations() = storeLocationsService.getAllStoreLocations().map { it.toResponse() }

    @GetMapping("/details/{storeLocationId}")
    fun getStoreLocationById(
        @PathVariable("storeLocationId") storeLocationId: Long = 1
    ): ResponseEntity<StoreLocationResponse> {
        return ResponseEntity(storeLocationsService.findStoreLocationById(storeLocationId)?.toResponse(), HttpStatus.OK)
    }

    @PostMapping("/near-me")
    fun findNearbyStores(
        @Valid @RequestBody geofenceData: GeoFenceEnterRequest
    ): List<StoreLocationResponse> {
        return storeLocationsService.findNearbyStores(geofenceData)
    }
}
