package com.project.notification.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.project.common.data.requests.notifications.DeepLinkNotificationRequest
import com.project.notification.entities.UserDeviceEntity
import com.project.notification.repositories.UserDeviceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Service for sending promotion and card product deep link notifications via Firebase Cloud Messaging
 */
@Service
class DeepLinkNotificationService(
    private val firebaseMessaging: FirebaseMessaging,
    private val userDeviceRepository: UserDeviceRepository
) {
    private val logger = LoggerFactory.getLogger(DeepLinkNotificationService::class.java)

    /**
     * Send promotion deep link notification to a specific user
     */
    fun sendPromotionDeepLinkNotification(
        userId: Long,
        promotionId: String,
        promotionTitle: String,
        promotionMessage: String
    ) {
        val userDevice = userDeviceRepository.findByUserId(userId)
            ?: run {
                logger.warn("No device found for user $userId")
                return
            }

        try {
            val deepLink = "nbkcapstone://promotion/$promotionId"
            val targetScreen = "promotion"
            val parameters = mapOf("promotionId" to promotionId)

            val message = Message.builder()
                .setToken(userDevice.firebaseToken)
                .putData("title", promotionTitle)
                .putData("body", promotionMessage)
                .putData("deepLink", deepLink)
                .putData("targetScreen", targetScreen)
                .putData("requiresAuth", "true")
                .putData("parameters", parameters.toString())
                .setNotification(
                    Notification.builder()
                        .setTitle(promotionTitle)
                        .setBody(promotionMessage)
                        .build()
                )
                .build()

            val response = firebaseMessaging.send(message)
            logger.info("Sent promotion deep link notification to user $userId: $response")
        } catch (e: Exception) {
            logger.error("Failed to send promotion deep link notification to user $userId: ${e.message}")
            throw e
        }
    }

    /**
     * Send card product deep link notification to a specific user
     */
    fun sendCardProductDeepLinkNotification(
        userId: Long,
        cardProductId: String,
        cardProductTitle: String,
        cardProductMessage: String
    ) {
        val userDevice = userDeviceRepository.findByUserId(userId)
            ?: run {
                logger.warn("No device found for user $userId")
                return
            }

        try {
            val deepLink = "nbkcapstone://card-product/$cardProductId"
            val targetScreen = "card-product"
            val parameters = mapOf("cardProductId" to cardProductId)

            val message = Message.builder()
                .setToken(userDevice.firebaseToken)
                .putData("title", cardProductTitle)
                .putData("body", cardProductMessage)
                .putData("deepLink", deepLink)
                .putData("targetScreen", targetScreen)
                .putData("requiresAuth", "true")
                .putData("parameters", parameters.toString())
                .setNotification(
                    Notification.builder()
                        .setTitle(cardProductTitle)
                        .setBody(cardProductMessage)
                        .build()
                )
                .build()

            val response = firebaseMessaging.send(message)
            logger.info("Sent card product deep link notification to user $userId: $response")
        } catch (e: Exception) {
            logger.error("Failed to send card product deep link notification to user $userId: ${e.message}")
            throw e
        }
    }

    /**
     * Send promotion deep link notification to multiple users
     */
    fun sendPromotionDeepLinkNotificationToMultipleUsers(
        userIds: List<Long>,
        promotionId: String,
        promotionTitle: String,
        promotionMessage: String
    ) {
        val userDevices = userDeviceRepository.findByUserIdIn(userIds)
        
        if (userDevices.isEmpty()) {
            logger.warn("No devices found for users: $userIds")
            return
        }

        val deepLink = "nbkcapstone://promotion/$promotionId"
        val targetScreen = "promotion"
        val parameters = mapOf("promotionId" to promotionId)

        userDevices.forEach { userDevice ->
            try {
                val message = Message.builder()
                    .setToken(userDevice.firebaseToken)
                    .putData("title", promotionTitle)
                    .putData("body", promotionMessage)
                    .putData("deepLink", deepLink)
                    .putData("targetScreen", targetScreen)
                    .putData("requiresAuth", "true")
                    .putData("parameters", parameters.toString())
                    .setNotification(
                        Notification.builder()
                            .setTitle(promotionTitle)
                            .setBody(promotionMessage)
                            .build()
                    )
                    .build()

                val response = firebaseMessaging.send(message)
                logger.info("Sent promotion deep link notification to user ${userDevice.userId}: $response")
            } catch (e: Exception) {
                logger.error("Failed to send promotion deep link notification to user ${userDevice.userId}: ${e.message}")
            }
        }
    }

    /**
     * Send card product deep link notification to multiple users
     */
    fun sendCardProductDeepLinkNotificationToMultipleUsers(
        userIds: List<Long>,
        cardProductId: String,
        cardProductTitle: String,
        cardProductMessage: String
    ) {
        val userDevices = userDeviceRepository.findByUserIdIn(userIds)
        
        if (userDevices.isEmpty()) {
            logger.warn("No devices found for users: $userIds")
            return
        }

        val deepLink = "nbkcapstone://card-product/$cardProductId"
        val targetScreen = "card-product"
        val parameters = mapOf("cardProductId" to cardProductId)

        userDevices.forEach { userDevice ->
            try {
                val message = Message.builder()
                    .setToken(userDevice.firebaseToken)
                    .putData("title", cardProductTitle)
                    .putData("body", cardProductMessage)
                    .putData("deepLink", deepLink)
                    .putData("targetScreen", targetScreen)
                    .putData("requiresAuth", "true")
                    .putData("parameters", parameters.toString())
                    .setNotification(
                        Notification.builder()
                            .setTitle(cardProductTitle)
                            .setBody(cardProductMessage)
                            .build()
                    )
                    .build()

                val response = firebaseMessaging.send(message)
                logger.info("Sent card product deep link notification to user ${userDevice.userId}: $response")
            } catch (e: Exception) {
                logger.error("Failed to send card product deep link notification to user ${userDevice.userId}: ${e.message}")
            }
        }
    }
} 