package com.project.notification.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.project.common.data.requests.notifications.AccountScoreNotification
import com.project.common.data.requests.notifications.BleBeaconNotificationDto
import com.project.common.data.requests.notifications.GeofenceNotificationDto
import com.project.common.enums.ErrorCode
import com.project.common.enums.NotificationDeliveryType
import com.project.common.enums.NotificationTriggerType
import com.project.common.exceptions.APIException
import com.project.common.exceptions.notifications.NotificationNotFoundException
import com.project.notification.entities.NotificationEntity
import com.project.notification.entities.UserDeviceEntity
import com.project.notification.repositories.NotificationRepository
import com.project.notification.repositories.UserDeviceRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class NotificationServiceImpl(
    private val firebaseMessaging: FirebaseMessaging,
    private val userDeviceRepository: UserDeviceRepository,
    private val notificationRepository: NotificationRepository,
) : NotificationService {
    private val logger = LoggerFactory.getLogger(NotificationService::class.java)


    // TODO("ensure unique entries for all notifications")
    override fun getAllNotificationsByUserId(userId: Long): List<NotificationEntity> {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
    }

    override fun getNotificationById(userId: Long, notificationId: Long): NotificationEntity {
        val notification = notificationRepository.findByIdOrNull(notificationId)
            ?: throw NotificationNotFoundException()

        if (userId != notification.userId) {
            throw APIException(
                message = "User does not have access to this resource",
                httpStatus = HttpStatus.FORBIDDEN,
                code = ErrorCode.ACCESS_DENIED,
            )
        }

        return notification
    }


    override fun processGeofenceEvent(event: GeofenceNotificationDto) {
        logger.info("Processing geofence event: ${event.message} - ${event.userId}")
        logger.info("[+]: ${event.userId}")
        val userDevice = userDeviceRepository.findByUserId(event.userId) ?: return

        notificationRepository.save(NotificationEntity(
            userId = event.userId,
            title = "Promotions Nearby",
            message = event.message,
            deliveryType = NotificationDeliveryType.PUSH,
            delivered = true,
            partnerId = event.partnerId,
            eventId = null,
            recommendationId = event.recommendationId,
            promotionId = event.promotionId,
            triggerType = NotificationTriggerType.GPS,
        ))
        sendGeofenceNotification(event, userDevice)
    }

    override fun processBleNotification(event: BleBeaconNotificationDto) {
        logger.info("Processing ble event: ${event.message} - ${event.userId}")
        logger.info("[+]: ${event.userId}")
        val userDevice = userDeviceRepository.findByUserId(event.userId) ?: return
        val title = if (event.promotionId == null) { event.message.split("at ").last() } else { "Promotions Nearby" }
        sendBleNotification(event, userDevice)

        notificationRepository.save(NotificationEntity(
            userId = event.userId,
            title = title,
            message = event.message,
            deliveryType = NotificationDeliveryType.PUSH,
            delivered = true,
            partnerId = event.partnerId,
            eventId = null,
            recommendationId = event.recommendationId,
            promotionId = event.promotionId,
            triggerType = NotificationTriggerType.GPS,
        ))
    }

    override fun processAccountScoreNotification(event: AccountScoreNotification) {
        logger.info("Processing account score event: ${event.message} - ${event.userId}")
        logger.info("[+]: ${event.userId}")
        val userDevice = userDeviceRepository.findByUserId(event.userId) ?: throw APIException(
            message = "User Firebasetoken not found",
            httpStatus = HttpStatus.NOT_FOUND,
            code = ErrorCode.FIREBASE_TOKEN_NOT_FOUND,
        )
        sendAccountScoreNotification(event, userDevice)

        notificationRepository.save(
            NotificationEntity(
                userId = event.userId,
                title = event.title,
                message = event.message,
                deliveryType = NotificationDeliveryType.PUSH,
                createdAt = LocalDateTime.now(),
                delivered = true,
                partnerId = null,
                eventId = null,
                recommendationId = event.recommendationId,
                promotionId = null,
                triggerType = NotificationTriggerType.POS
            )
        )
    }


    override fun notificationByTypeSentToUserToday(
        userId: Long,
        partnerId: Long
    ): NotificationEntity? {
        val today = LocalDate.now()
        val startOfDay = today.atStartOfDay()
        val endOfDay = today.plusDays(1).atStartOfDay()

        val notification = notificationRepository.findByUserIdAndPartnerIdAndNotificationTypeOnSentDate(
            userId = userId,
            partnerId = partnerId,
            startOfDay = startOfDay,
            endOfDay = endOfDay
        )
        return notification
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

    private fun sendAccountScoreNotification(
        event: AccountScoreNotification,
        userDevice: UserDeviceEntity
    ) {
        try {
            val message = Message.builder()
                .setToken(userDevice.firebaseToken)
                .putData("title", event.title)
                .putData("body", event.message)
                .setNotification(
                    Notification.builder()
                        .setTitle(event.title)
                        .setBody(event.message)
                        .build()
                )
                .build()

            val response = firebaseMessaging.send(message)
            logger.info("Sent account sore notification: $response")
        } catch (e: Exception) {
            logger.error("Failed to send notification to device ${userDevice.id}: ${e.message}")
        }
    }
}
