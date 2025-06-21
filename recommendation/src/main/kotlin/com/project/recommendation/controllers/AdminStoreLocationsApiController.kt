package com.project.recommendation.controllers

import com.project.common.data.requests.storeLocations.StoreLocationCreateRequest
import com.project.common.data.responses.storeLocations.StoreLocationResponse
import com.project.recommendation.entities.StoreLocationEntity
import com.project.recommendation.mappers.toResponse
import com.project.recommendation.services.StoreLocationsService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin/store-locations")
class AdminStoreLocationsApiController(
    private val storeLocationsService: StoreLocationsService,
) {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun createStoreLocation(
        @Valid @RequestBody newLocation: StoreLocationCreateRequest
    ): ResponseEntity<StoreLocationResponse> {
        return ResponseEntity(storeLocationsService.createStoreLocation(newLocation).toResponse(),HttpStatus.CREATED)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{storeLocationId}")
    fun deleteStoreLocationById(
        @PathVariable("storeLocationId") storeLocationId: Long
    ): ResponseEntity<Unit> {
        storeLocationsService.deleteStoreLocationById(storeLocationId)
        return ResponseEntity.noContent().build()
    }
}