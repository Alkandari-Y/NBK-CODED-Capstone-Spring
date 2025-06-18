package com.project.notification.services

import com.project.common.exceptions.auth.UserNotFoundException
import com.project.notification.entities.UserDeviceEntity
import com.project.notification.repositories.UserDeviceRepository
import org.springframework.stereotype.Service

@Service
class UserDeviceService(
    private val userDeviceRepository: UserDeviceRepository
) {
    fun registerFirebaseToken(userId: Long, firebaseToken: String) {
        val user = userDeviceRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User not found") }

        val existingDevice = userDeviceRepository.findByUserId(userId)
        if (existingDevice != null) {
            existingDevice.firebaseToken = firebaseToken
            userDeviceRepository.save(existingDevice)
        } else {
            val newDevice = UserDeviceEntity(userId = user.id!!, firebaseToken = firebaseToken)
            userDeviceRepository.save(newDevice)
        }
    }
}
