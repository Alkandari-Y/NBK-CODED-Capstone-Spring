package com.project.notification.repositories

import com.project.common.enums.NotificationDeliveryType
import com.project.notification.entities.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<NotificationEntity, Long> {

    // all notifications ordered by creation date
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<NotificationEntity>

    // unread notifications for a user
    fun findByUserIdAndDeliveredOrderByCreatedAtDesc(
        userId: Long,
        delivered: Boolean
    ): List<NotificationEntity>

}