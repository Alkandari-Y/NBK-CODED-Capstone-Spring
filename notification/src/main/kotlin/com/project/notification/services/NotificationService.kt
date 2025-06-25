package com.project.notification.services

import com.project.notification.entities.NotificationEntity

interface NotificationService {
    fun getAllNotificationsByUserId(userId: Long): List<NotificationEntity>
    fun getUnreadNotificationsByUserId(userId: Long): List<NotificationEntity>
    fun getNotificationById(notificationId: Long): NotificationEntity?
    fun markNotificationsAsRead(userId: Long, notificationIds: List<Long>? = null): List<NotificationEntity>
}
