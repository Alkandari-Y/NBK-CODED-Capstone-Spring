package com.project.notification.repositories

import com.project.notification.entities.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface NotificationRepository: JpaRepository<NotificationEntity, Long> {
}