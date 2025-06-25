package com.project.notification.controllers

import com.project.notification.entities.NotificationEntity
import com.project.notification.services.NotificationService
import com.project.notification.mappers.toResponseDto
import com.project.common.data.responses.notifications.NotificationResponseDto
import com.project.common.exceptions.NotificationNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationApiController(
    private val notificationService: NotificationService
) {

    @GetMapping("/user/{userId}")
    fun getAllNotificationsByUserId(
        @PathVariable userId: Long
    ): ResponseEntity<List<NotificationResponseDto>> {
        val notifications = notificationService.getAllNotificationsByUserId(userId)
        return ResponseEntity(notifications.map { it.toResponseDto() }, HttpStatus.OK)
    }

    @GetMapping("/user/{userId}/unread")
    fun getUnreadNotificationsByUserId(
        @PathVariable userId: Long
    ): ResponseEntity<List<NotificationResponseDto>> {
        val unreadNotifications = notificationService.getUnreadNotificationsByUserId(userId)
        return ResponseEntity(unreadNotifications.map { it.toResponseDto() }, HttpStatus.OK)
    }

    @GetMapping("/{notificationId}")
    fun getNotificationById(
        @PathVariable notificationId: Long
    ): ResponseEntity<NotificationResponseDto> {
        val notification = notificationService.getNotificationById(notificationId)
            ?: throw NotificationNotFoundException()
        return ResponseEntity(notification.toResponseDto(), HttpStatus.OK)
    }

    @PutMapping("/user/{userId}/mark-read")
    fun markNotificationsAsRead(
        @PathVariable userId: Long,
        @RequestBody(required = false) notificationIds: List<Long>? = null
    ): ResponseEntity<List<NotificationResponseDto>> {
        val updatedNotifications = notificationService.markNotificationsAsRead(userId, notificationIds)
        return ResponseEntity(updatedNotifications.map { it.toResponseDto() }, HttpStatus.OK)
    }
}