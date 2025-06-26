package com.project.banking.providers

import com.project.common.data.requests.accountProducts.AccountProductRecDto
import com.project.common.data.responses.RecommendationDto
import com.project.common.data.responses.promotions.PromotionResponse
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
    fun initializeCategoryScores(userId: Long): Boolean {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val requestBody = mapOf("userId" to userId)
        val request = HttpEntity(requestBody, headers)

        return try {
            val response = RestTemplate().exchange(
                "$recServiceUrl/category-scores",
                HttpMethod.POST,
                request,
                object : ParameterizedTypeReference<Void>() {}
            )
            response.statusCode.is2xxSuccessful
        } catch (ex: HttpClientErrorException) {
            false
        }
    }

    fun triggerAccountScoreNotif(recDto: AccountProductRecDto): Boolean {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity(recDto, headers)

        return try {
            val response = RestTemplate().exchange(
                "$recServiceUrl/recommendations/account-score",
                HttpMethod.POST,
                request,
                object : ParameterizedTypeReference<Void>() {}
            )
            response.statusCode.is2xxSuccessful
        } catch (ex: HttpClientErrorException) {
            false
        }
    }

    fun incrementCategoryFrequency(userId: Long, categoryId: Long): Boolean {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val requestBody = mapOf("userId" to userId, "categoryId" to categoryId)
        val request = HttpEntity(requestBody, headers)

        return try {
            val response = RestTemplate().exchange(
                "$recServiceUrl/category-scores/increment",
                HttpMethod.POST,
                request,
                object : ParameterizedTypeReference<Void>() {}
            )
            response.statusCode.is2xxSuccessful
        } catch (ex: HttpClientErrorException) {
            false
        }
    }

    fun getActivePromotionsByBusiness(businessId: Long): List<PromotionResponse> {
        return try {
            val response = RestTemplate().exchange(
                "$recServiceUrl/promotions/business/$businessId/active",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<PromotionResponse>>() {}
            )
            response.body ?: emptyList()
        } catch (ex: HttpClientErrorException) {
            emptyList()
        }
    }
}