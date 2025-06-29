package com.project.notification.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.project.common.data.requests.notifications.BleBeaconNotificationDto
import com.project.common.data.requests.notifications.GeofenceNotificationDto
import com.project.common.enums.NotificationTriggerType
import com.project.notification.entities.NotificationEntity
import com.project.notification.entities.UserDeviceEntity
import com.project.notification.repositories.NotificationRepository
import com.project.notification.repositories.UserDeviceRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface NotificationService{

    fun getAllNotificationsByUserId(userId: Long): List<NotificationEntity>
    fun getNotificationById(userId: Long, notificationId: Long): NotificationEntity
    fun processGeofenceEvent(event: GeofenceNotificationDto)
    fun processBleNotification(event: BleBeaconNotificationDto)
    fun processAccountScoreNotification(event: AccountScoreNotification)
    fun notificationByTypeSentToUserToday(
        userId: Long,
        notificationTriggerType: NotificationTriggerType,
        partnerId: Long
    ): NotificationEntity?
}

data class AccountScoreNotification(
    val userId: Long,
    val accountProductName: String,
    val accountProductId: Long,
    val recommendationId: Long,
    val title: String,
    val message: Message
)