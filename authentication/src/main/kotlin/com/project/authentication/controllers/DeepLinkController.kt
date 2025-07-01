package com.project.authentication.controllers

import com.project.common.data.requests.deeplink.DeepLinkRequest
import com.project.common.data.responses.deeplink.DeepLinkResponse
import com.project.authentication.services.DeepLinkService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for handling deep link requests from mobile app
 * Supports nbkcapstone:// scheme for navigation
 */
@RestController
@RequestMapping("/api/v1/deeplink")
class DeepLinkController(
    private val deepLinkService: DeepLinkService
) {

    /**
     * Process deep link request and return appropriate response
     * This endpoint is called by the mobile app to validate and process deep links
     */
    @PostMapping("/process")
    fun processDeepLink(
        @Valid @RequestBody request: DeepLinkRequest
    ): ResponseEntity<DeepLinkResponse> {
        val response = deepLinkService.processDeepLink(request)
        return ResponseEntity.ok(response)
    }

    /**
     * Generate deep link URLs for sharing
     */
    @PostMapping("/generate")
    fun generateDeepLink(
        @Valid @RequestBody request: DeepLinkRequest
    ): ResponseEntity<DeepLinkResponse> {
        val response = deepLinkService.generateDeepLink(request)
        return ResponseEntity.ok(response)
    }

    /**
     * Validate deep link format
     */
    @PostMapping("/validate")
    fun validateDeepLink(
        @Valid @RequestBody request: DeepLinkRequest
    ): ResponseEntity<DeepLinkResponse> {
        val response = deepLinkService.validateDeepLink(request)
        return ResponseEntity.ok(response)
    }
} 