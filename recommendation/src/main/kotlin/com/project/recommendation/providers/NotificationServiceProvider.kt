package com.project.recommendation.providers

import com.project.common.data.requests.notifications.NotificationDto
import com.project.common.exceptions.APIException
import jakarta.inject.Named
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate


@Named
class NotificationProvider(
    @Value("\${notificationServiceBase.url}") private val notificationServiceURL: String
) {
    fun sendNotification(notification: NotificationDto) {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<NotificationDto>(notification, headers)

        try {
            val response = RestTemplate().exchange(
                "$notificationServiceURL/geofence/event",
                HttpMethod.POST,
                request,
                Void::class.java
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            throw APIException("error fetching business partners")
        }
    }

    fun sendGeoFencedNotification(notification: NotificationDto) {
        println("Sending geo fenced notification to $notificationServiceURL")
    }

}