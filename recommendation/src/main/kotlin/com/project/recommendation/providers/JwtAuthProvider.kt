package com.project.recommendation.providers

import com.project.common.exceptions.auth.InvalidTokenException
import com.project.common.data.responses.authentication.ValidateTokenResponse
import jakarta.inject.Named
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.web.client.RestTemplate

@Named
class JwtAuthProvider (
    @Value("\${authServiceBase.url}")
    private val authServiceURL: String
){
    fun authenticateToken(token: String): ValidateTokenResponse {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(token)
        }

        val request = HttpEntity<String>(null, headers)

        val response = RestTemplate().exchange(
            "$authServiceURL/api/v1/auth/validate",
            HttpMethod.POST,
            request,
            object : ParameterizedTypeReference<ValidateTokenResponse>() {}
        )

        return response.body ?: throw InvalidTokenException()
    }
}