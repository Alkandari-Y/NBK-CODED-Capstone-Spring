package com.project.notification.controllers

import com.project.common.data.requests.geofencing.GeofenceEventRequest
import com.project.notification.services.GeofenceEventService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/geofence")
class GeofenceEventController(
    private val geofenceEventService: GeofenceEventService
) {

    @PostMapping("/event")
    fun handleGeofenceEvent(@Valid @RequestBody request: GeofenceEventRequest): ResponseEntity<Unit> {
        geofenceEventService.processGeofenceEvent(request)
        return ResponseEntity.ok().build()
    }
} 