package com.project.recommendation.controllers

import com.project.common.data.responses.storeLocations.StoreLocationResponse
import com.project.recommendation.mappers.toResponse
import com.project.recommendation.services.StoreLocationsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/store-locations")
class StoreLocationsApiController(
    private val storeLocationsService: StoreLocationsService,
) {
    @RequestMapping
    fun getAllStoreLocations() = storeLocationsService.getAllStoreLocations().map { it.toResponse() }

    @RequestMapping("/{storeLocationId}")
    fun getStoreLocationById(
        @PathVariable("storeLocationId") storeLocationId: Long
    ): ResponseEntity<StoreLocationResponse> {
        return ResponseEntity(storeLocationsService.findStoreLocationById(storeLocationId)?.toResponse(), HttpStatus.OK)
    }
}
