package com.project.notification.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.project.notification.data.GeofenceEventRequest
import com.project.notification.repositories.UserDeviceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GeofenceEventService(
    private val firebaseMessaging: FirebaseMessaging,
    private val userDeviceRepository: UserDeviceRepository
) {
    private val logger = LoggerFactory.getLogger(GeofenceEventService::class.java)

    fun processGeofenceEvent(event: GeofenceEventRequest) {
        logger.info("Processing geofence event: ${event.name} - ${event.transitionType}")
        logger.info("[+]: ${event.userId}")

        // TODO: Add your business logic here
        // For example: log the visit, trigger recommendations, etc.
        
        // Send a notification to the user about the geofence event
        sendGeofenceNotification(event)
    }

    private fun sendGeofenceNotification(event: GeofenceEventRequest) {
        // For now, we'll send to all devices. In production, you'd filter by user
        val devices = userDeviceRepository.findAll()
        
        devices.forEach { device ->
            try {
                val message = Message.builder()
                    .setToken(device.firebaseToken)
                    .putData("title", "Location Update")
                    .putData("body", "You ${event.transitionType.lowercase()} ${event.name}")
                    .putData("geofenceId", event.id.toString())
                    .putData("transitionType", event.transitionType)
                    .putData("mallName", event.name)
                    .build()

                val response = firebaseMessaging.send(message)
                logger.info("Sent geofence notification: $response")
            } catch (e: Exception) {
                logger.error("Failed to send notification to device ${device.id}: ${e.message}")
            }
        }
    }
} 