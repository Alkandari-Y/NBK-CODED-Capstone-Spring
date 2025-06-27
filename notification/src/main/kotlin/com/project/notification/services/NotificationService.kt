package com.project.notification.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.project.common.data.requests.notifications.BleBeaconNotificationDto
import com.project.common.data.requests.notifications.GeofenceNotificationDto
import com.project.notification.entities.UserDeviceEntity
import com.project.notification.repositories.UserDeviceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val firebaseMessaging: FirebaseMessaging,
    private val userDeviceRepository: UserDeviceRepository
) {
    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun processGeofenceEvent(event: GeofenceNotificationDto) {
        logger.info("Processing geofence event: ${event.message} - ${event.userId}")
        logger.info("[+]: ${event.userId}")
        val userDevice = userDeviceRepository.findByUserId(event.userId) ?: return

        sendGeofenceNotification(event, userDevice)
    }

    fun processBleNotification(event: BleBeaconNotificationDto) {
        logger.info("Processing ble event: ${event.message} - ${event.userId}")
        logger.info("[+]: ${event.userId}")
        val userDevice = userDeviceRepository.findByUserId(event.userId) ?: return

        sendBleNotification(event, userDevice)
    }

    private fun sendGeofenceNotification(
        event: GeofenceNotificationDto,
        userDevice: UserDeviceEntity,
    ) {

        try {
            val message = Message.builder()
                .setToken(userDevice.firebaseToken)
                .putData("title", "Promotions Nearby")
                .putData("body", event.message)
                .putData("someId", 1.toString())
                .setNotification(
                    Notification.builder()
                        .setTitle("Promotions Nearby")
                        .setBody(event.message)
                        .build()
                )
                .build()

            val response = firebaseMessaging.send(message)
            logger.info("Sent geofence notification: $response")
        } catch (e: Exception) {
            logger.error("Failed to send notification to device ${userDevice.id}: ${e.message}")
        }
    }

    private fun sendBleNotification(
        event: BleBeaconNotificationDto,
        userDevice: UserDeviceEntity,
    ) {
        val title = if (event.promotionId == null) {
            "Earn Cashback and Discounts"
        } else {
            "Promotions Nearby"
        }
        try {
            val message = Message.builder()
                .setToken(userDevice.firebaseToken)
                .putData("title", title)
                .putData("body", event.message)
                .putData("someId", 1.toString())
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(event.message)
                        .build()
                )
                .build()

            val response = firebaseMessaging.send(message)
            logger.info("Sent geofence notification: $response")
        } catch (e: Exception) {
            logger.error("Failed to send notification to device ${userDevice.id}: ${e.message}")
        }
    }

} 