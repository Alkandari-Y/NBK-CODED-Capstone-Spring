package com.project.common.data.requests.deeplink

import jakarta.validation.constraints.NotBlank

/**
 * Request DTO for deep link processing
 */
data class DeepLinkRequest(
    /**
     * The deep link URL to process (e.g., "nbkcapstone://wallet")
     */
    @field:NotBlank(message = "Deep link is required")
    val deepLink: String? = null,
    
    /**
     * Target screen for generating deep links
     */
    val targetScreen: String? = null,
    
    /**
     * Additional parameters for the deep link (e.g., promotionId)
     */
    val parameters: Map<String, String>? = null
) 