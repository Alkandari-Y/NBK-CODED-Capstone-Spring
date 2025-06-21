package com.project.notification.services

import com.project.common.data.requests.firebase.UserDeviceFBTokenRequest
import com.project.notification.entities.UserDeviceEntity
import com.project.notification.repositories.UserDeviceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDeviceService(
    private val userDeviceRepository: UserDeviceRepository
) {

    @Transactional
    fun registerFirebaseToken(userId: Long, fbTokenRequest: UserDeviceFBTokenRequest) {
        val tokenExists = userDeviceRepository.findByFirebaseToken(fbTokenRequest.firebaseToken)

        if (tokenExists != null) {
            userDeviceRepository.deleteByFirebaseToken(fbTokenRequest.firebaseToken)
        }

        val newUserDevice = userDeviceRepository.findByUserId(userId)
        if (newUserDevice != null) {
            userDeviceRepository.save(newUserDevice.copy(firebaseToken = fbTokenRequest.firebaseToken))
        } else {
            userDeviceRepository.save(UserDeviceEntity(
                userId = userId,
                firebaseToken = fbTokenRequest.firebaseToken
            ))
        }
    }
}
