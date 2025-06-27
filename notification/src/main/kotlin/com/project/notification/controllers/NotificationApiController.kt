package com.project.notification.controllers

import com.project.common.data.requests.notifications.BleBeaconNotificationDto
import com.project.common.data.requests.notifications.GeofenceNotificationDto
import com.project.notification.services.NotificationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/notifications")
class BeaconApiController(
    private val notificationService: NotificationService
) {
    @PostMapping("/geofence/entered")
    fun handleGeofenceEvent(@Valid @RequestBody request: GeofenceNotificationDto): ResponseEntity<Unit> {
        notificationService.processGeofenceEvent(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/ble")
    fun handleGeofenceEvent(@Valid @RequestBody request: BleBeaconNotificationDto): ResponseEntity<Unit> {
        notificationService.processBleNotification(request)
        return ResponseEntity.ok().build()
    }
}