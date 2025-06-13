package com.project.authentication.filters

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class LoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val cachedRequest = ContentCachingRequestWrapper(request)
        val cachedResponse = ContentCachingResponseWrapper(response)

        filterChain.doFilter(cachedRequest, cachedResponse)

        cachedResponse.copyBodyToResponse()

        logRequest(cachedRequest)
        logResponse(cachedResponse)
    }

    private fun logRequest(request: ContentCachingRequestWrapper) {
        val requestBody = String(request.contentAsByteArray)
        val uri = request.requestURI
        val method = request.method

        val sanitizedBody = try {
            val mapper = jacksonObjectMapper()
            val bodyMap: MutableMap<String, Any?> = mapper.readValue(requestBody)
            if ("password" in bodyMap) {
                bodyMap["password"] = "***"
            }
            mapper.writeValueAsString(bodyMap)
        } catch (e: Exception) {
            requestBody
        }

        logger.info("Request: method = $method uri = $uri body = $sanitizedBody")
    }

    private fun logResponse(response: ContentCachingResponseWrapper) {
        val responseBody = String(response.contentAsByteArray)
        logger.info("Response: status = ${response.status} body = $responseBody")
    }
}
