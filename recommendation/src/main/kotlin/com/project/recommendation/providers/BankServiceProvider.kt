package com.project.recommendation.providers

import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import com.project.common.exceptions.businessPartner.BusinessNotFoundException
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
class BankServiceProvider(
    @Value("\${bankServiceBase.url}") private val bankServiceURL: String
) {
    fun getBusinessPartner(businessPartnerId: Long): BusinessPartnerDto {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<String>(null, headers)

        return try {
            val response = RestTemplate().exchange(
                "$bankServiceURL/partners/$businessPartnerId",
                HttpMethod.GET,
                request,
                object : ParameterizedTypeReference<BusinessPartnerDto>() {}
            )
            response.body ?: throw BusinessNotFoundException()
        } catch (ex: HttpClientErrorException.NotFound) {
            throw BusinessNotFoundException()
        }
    }
}