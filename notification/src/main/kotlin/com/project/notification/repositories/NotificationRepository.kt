package com.project.notification.repositories

import com.project.notification.entities.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<NotificationEntity, Long> {

    // all notifications ordered by creation date
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<NotificationEntity>

}