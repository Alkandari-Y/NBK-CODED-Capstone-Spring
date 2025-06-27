package com.project.notification.controllers

import com.project.common.data.requests.notifications.BleBeaconNotificationDto
import com.project.common.data.requests.notifications.GeofenceNotificationDto
import com.project.common.data.responses.notifications.NotificationResponseDto
import com.project.common.security.RemoteUserPrincipal
import com.project.notification.mappers.toResponseDto
import com.project.notification.services.NotificationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/notifications")
class NotificationApiController(
    private val notificationService: NotificationService
) {
    @GetMapping
    fun getAllNotifications(
        @AuthenticationPrincipal user: RemoteUserPrincipal
    ): ResponseEntity<List<NotificationResponseDto>> {
        val notifications = notificationService.getAllNotificationsByUserId(user.getUserId())
        return ResponseEntity(notifications.map { it.toResponseDto() }, HttpStatus.OK)
    }

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