package com.project.notification.services

import com.project.notification.entities.NotificationEntity

interface NotificationService {
    fun getAllNotificationsByUserId(userId: Long): List<NotificationEntity>
    fun getNotificationById(notificationId: Long): NotificationEntity?
}