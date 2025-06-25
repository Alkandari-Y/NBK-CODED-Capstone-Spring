package com.project.notification.services

import com.project.notification.entities.NotificationEntity
import com.project.notification.repositories.NotificationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository
) : NotificationService {

    override fun getAllNotificationsByUserId(userId: Long): List<NotificationEntity> {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
    }

    override fun getUnreadNotificationsByUserId(userId: Long): List<NotificationEntity> {
        return notificationRepository.findByUserIdAndDeliveredOrderByCreatedAtDesc(userId, false)
    }

    override fun getNotificationById(notificationId: Long): NotificationEntity? {
        return notificationRepository.findByIdOrNull(notificationId)
    }

    @Transactional
    override fun markNotificationsAsRead(userId: Long, notificationIds: List<Long>?): List<NotificationEntity> {
        val notifications = if (notificationIds.isNullOrEmpty()) {
            // mark all unread notifications as read
            notificationRepository.findByUserIdAndDeliveredOrderByCreatedAtDesc(userId, false)
        } else {
            // mark specific notifications as read
            notificationRepository.findAllById(notificationIds)
                .filter { it.userId == userId && !it.delivered }
        }

        return notifications.map { notification ->
            notification.delivered = true
            notificationRepository.save(notification)
        }
    }
}