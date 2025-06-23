package com.project.recommendation.providers

import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.common.data.responses.businessPartners.BusinessPartnerDto
import com.project.common.data.responses.categories.CategoryDto
import com.project.common.data.responses.categories.CategoryWithPerksDto
import com.project.common.data.responses.kyc.KYCResponse
import com.project.common.exceptions.APIException
import com.project.common.exceptions.businessPartner.BusinessNotFoundException
import com.project.common.exceptions.kyc.KycNotFoundException
import jakarta.inject.Named
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
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

    fun getAllBusinessPartners(): List<BusinessPartnerDto> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<String>(null, headers)

        return try {
            val response = RestTemplate().exchange(
                "$bankServiceURL/partners",
                HttpMethod.GET,
                request,
                object : ParameterizedTypeReference<List<BusinessPartnerDto>>() {}
            )
            response.body ?: throw APIException("error fetching business partners")
        } catch (ex: HttpClientErrorException.NotFound) {
            throw APIException("error fetching business partners")
        }
    }

    fun getAllAccountProducts(): List<AccountProductDto> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<String>(null, headers)

        return try {
            val response = RestTemplate().exchange(
                "$bankServiceURL/products",
                HttpMethod.GET,
                request,
                object : ParameterizedTypeReference<List<AccountProductDto>>() {}
            )
            response.body ?: throw APIException("error fetching products")
        } catch (ex: HttpClientErrorException.NotFound) {
            throw APIException("error fetching products")
        }
    }

    fun getAllCategories(): List<CategoryWithPerksDto> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<String>(null, headers)

        return try {
            val response = RestTemplate().exchange(
                "$bankServiceURL/categories",
                HttpMethod.GET,
                request,
                object : ParameterizedTypeReference<List<CategoryWithPerksDto>>() {}
            )
            response.body ?: throw APIException("error fetching business categories")
        } catch (ex: HttpClientErrorException.NotFound) {
            throw APIException("error fetching business categories")
        }
    }

    fun getUserKyc(userId: Long): KYCResponse? {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity<String>(null, headers)

        return try {
            val response = RestTemplate().exchange(
                "$bankServiceURL/kyc/client/$userId",
                HttpMethod.GET,
                request,
                object : ParameterizedTypeReference<KYCResponse>() {}
            )
            response.body ?: throw KycNotFoundException(userId)
        } catch (ex: HttpClientErrorException.NotFound) {
            throw KycNotFoundException(userId)
        }
    }
}