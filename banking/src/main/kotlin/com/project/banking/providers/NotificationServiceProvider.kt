package com.project.banking.providers

import com.project.common.data.responses.notifications.NotificationResponseDto
import com.project.common.enums.NotificationTriggerType
import jakarta.inject.Named
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange


@Named
class NotificationServiceProvider(
    @Value("\${notificationServiceBase.url}") private val notificationServiceUrl: String,
) {
    fun getPromotionNotificationSentToUserForPartner(
        userId: Long,
        partnerId: Long,
    ): NotificationResponseDto? {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val request = HttpEntity<Void>(null, headers)

        val url = "$notificationServiceUrl/notifications/search" +
                "?userId=$userId&partnerId=$partnerId"

        return try {
            val response = RestTemplate().exchange<NotificationResponseDto>(
                url,
                HttpMethod.GET,
                request
            )
            response.body
        } catch (ex: HttpClientErrorException.NotFound) {
            null
        } catch (ex: HttpClientErrorException) {
            null
        }
    }
}