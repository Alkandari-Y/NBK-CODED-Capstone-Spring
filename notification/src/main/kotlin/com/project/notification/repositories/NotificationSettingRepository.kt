package com.project.notification.repositories

import com.project.notification.entities.NotificationSettingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationSettingRepository: JpaRepository<NotificationSettingEntity, Long> {
}