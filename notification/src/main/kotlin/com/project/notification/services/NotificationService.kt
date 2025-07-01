package com.project.notification.services


import com.project.common.data.requests.notifications.AccountScoreNotification
import com.project.common.data.requests.notifications.BleBeaconNotificationDto
import com.project.common.data.requests.notifications.GeofenceNotificationDto
import com.project.common.enums.NotificationTriggerType
import com.project.notification.entities.NotificationEntity


interface NotificationService{

    fun getAllNotificationsByUserId(userId: Long): List<NotificationEntity>
    fun getNotificationById(userId: Long, notificationId: Long): NotificationEntity
    fun processGeofenceEvent(event: GeofenceNotificationDto)
    fun processBleNotification(event: BleBeaconNotificationDto)
    fun processAccountScoreNotification(event: AccountScoreNotification)
    fun notificationByTypeSentToUserToday(
        userId: Long,
        partnerId: Long
    ): NotificationEntity?
}
