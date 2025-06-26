// File: com/project/notification/controllers/NotificationApiController.kt
package com.project.notification.controllers

import com.project.notification.entities.NotificationEntity
import com.project.notification.services.NotificationService
import com.project.notification.mappers.toResponseDto
import com.project.common.data.responses.notifications.NotificationResponseDto
import com.project.common.exceptions.notifications.NotificationNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
}