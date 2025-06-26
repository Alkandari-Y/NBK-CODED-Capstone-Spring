package com.project.notification.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.project.common.data.requests.geofencing.GeofenceEventRequest
import com.project.common.data.requests.notifications.NotificationDto
import com.project.notification.repositories.UserDeviceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GeofenceEventService(
    private val firebaseMessaging: FirebaseMessaging,
    private val userDeviceRepository: UserDeviceRepository
) {
    private val logger = LoggerFactory.getLogger(GeofenceEventService::class.java)

    fun processGeofenceEvent(event: NotificationDto) {
        logger.info("Processing geofence event: ${event.message} - ${event.userId}")
        logger.info("[+]: ${event.userId}")


        sendGeofenceNotification(event)
    }

    private fun sendGeofenceNotification(event: NotificationDto) {
        // For now, we'll send to all devices. In production, you'd filter by user
        val devices = userDeviceRepository.findAll()
        
        devices.forEach { device ->
            try {
                val message = Message.builder()
                    .setToken(device.firebaseToken)
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
                logger.error("Failed to send notification to device ${device.id}: ${e.message}")
            }
        }
    }
} 