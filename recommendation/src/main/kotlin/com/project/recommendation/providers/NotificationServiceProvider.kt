package com.project.recommendation.providers

import com.project.common.data.requests.notifications.AccountScoreNotification
import com.project.common.data.requests.notifications.BleBeaconNotificationDto
import com.project.common.data.requests.notifications.GeofenceNotificationDto
import com.project.common.exceptions.APIException
import jakarta.inject.Named
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate


@Named
class NotificationServiceProvider(
    @Value("\${notificationServiceBase.url}") private val notificationServiceURL: String
) {
    fun sendGeoFencedNotification(notification: GeofenceNotificationDto) {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<GeofenceNotificationDto>(notification, headers)

        try {
            val response = RestTemplate().exchange(
                "$notificationServiceURL/api/v1/notifications/geofence/entered",
                HttpMethod.POST,
                request,
                Void::class.java
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            throw APIException("error fetching sending notification")
        }
    }

    fun sendBledNotification(notification: BleBeaconNotificationDto) {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<BleBeaconNotificationDto>(notification, headers)

        try {
            val response = RestTemplate().exchange(
                "$notificationServiceURL/api/v1/notifications/ble",
                HttpMethod.POST,
                request,
                Void::class.java
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            throw APIException("error fetching sending notification")
        }
    }

    fun sendAccountProductRecommendationNotification(recommendation: AccountScoreNotification) {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<AccountScoreNotification>(recommendation, headers)

        try {
            RestTemplate().exchange(
                "$notificationServiceURL/api/v1/notifications/account-score",
                HttpMethod.POST,
                request,
                Void::class.java
            )
        } catch (ex: HttpClientErrorException.NotFound) {
            throw APIException("error fetching sending account recommendation")
        }
    }
}