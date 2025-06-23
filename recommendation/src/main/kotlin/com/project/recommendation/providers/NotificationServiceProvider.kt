package com.project.recommendation.providers

import com.project.common.enums.RecommendationType
import jakarta.inject.Named
import org.springframework.beans.factory.annotation.Value

data class SendNotificationRequest(val title: String, val body: String, val userId: Long, val recType: RecommendationType)

@Named
class NotificationProvider(
    @Value("\${notificationServiceBase.url}") private val notificationServiceURL: String
) {
    fun sendNotification(notification: String) {
        println("Sending notification to $notificationServiceURL")
    }

    fun sendGeoFencedNotification(notification: SendNotificationRequest) {
        println("Sending geo fenced notification to $notificationServiceURL")
    }

}