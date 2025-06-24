package com.project.banking.providers

import com.project.common.data.requests.accountProducts.AccountProductRecDto
import com.project.common.data.responses.RecommendationDto
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
class RecommendationServiceProvider(
    @Value("\${recommendationServiceBase.url}") private val recServiceUrl: String
) {
    fun updateUserCategoryScores(userId: Long, categoryId: Long) {

    }

    fun recommendNewCard(accountProductRecDto: AccountProductRecDto): RecommendationDto? {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity(accountProductRecDto, headers)

        return try {
            val response = RestTemplate().exchange(
                "$recServiceUrl/recommendations/account-score",
                HttpMethod.POST,
                request,
                object : ParameterizedTypeReference<RecommendationDto>() {}
            )
            response.body
        } catch (ex: HttpClientErrorException) {
            null
        }
    }
}