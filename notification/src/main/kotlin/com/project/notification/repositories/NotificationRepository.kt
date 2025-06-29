package com.project.notification.repositories

import com.project.common.enums.NotificationTriggerType
import com.project.notification.entities.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
interface NotificationRepository : JpaRepository<NotificationEntity, Long> {

    // all notifications ordered by creation date
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<NotificationEntity>

    @Query(
        """
    SELECT n FROM NotificationEntity n
    WHERE n.userId = :userId
      AND n.partnerId = :partnerId
      AND n.triggerType = :triggerType 
      AND n.createdAt >= :startOfDay 
      AND n.createdAt < :endOfDay
"""
    )
    fun findByUserIdAndPartnerIdAndNotificationTypeOnSentDate(
        @Param("userId") userId: Long,
        @Param("partnerId") partnerId: Long,
        @Param("triggerType") triggerType: NotificationTriggerType,
        @Param("startOfDay") startOfDay: LocalDateTime,
        @Param("endOfDay") endOfDay: LocalDateTime
    ): NotificationEntity?
}