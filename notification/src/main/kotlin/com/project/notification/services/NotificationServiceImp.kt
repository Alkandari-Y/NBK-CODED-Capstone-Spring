package com.project.notification.services

import com.project.notification.entities.NotificationEntity
import com.project.notification.repositories.NotificationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository
) : NotificationService {

    override fun getAllNotificationsByUserId(userId: Long): List<NotificationEntity> {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
    }

    override fun getNotificationById(notificationId: Long): NotificationEntity? {
        return notificationRepository.findByIdOrNull(notificationId)
    }
}
