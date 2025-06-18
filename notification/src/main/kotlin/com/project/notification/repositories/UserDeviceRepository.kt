package com.project.notification.repositories

import com.project.notification.entities.UserDeviceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDeviceRepository: JpaRepository<UserDeviceEntity, Long> {
    fun findByUserId(userId: Long): UserDeviceEntity?
}