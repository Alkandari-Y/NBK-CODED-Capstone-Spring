package com.project.common.data.requests.notifications

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * Request DTO for sending deep link notifications via Firebase Cloud Messaging
 */
data class DeepLinkNotificationRequest(
    /**
     * User ID to send the notification to
     */
    @field:NotNull(message = "User ID is required")
    val userId: Long,
    
    /**
     * Notification title
     */
    @field:NotBlank(message = "Title is required")
    val title: String,
    
    /**
     * Notification message
     */
    @field:NotBlank(message = "Message is required")
    val message: String,
    
    /**
     * Deep link URL (e.g., "nbkcapstone://wallet")
     */
    @field:NotBlank(message = "Deep link is required")
    val deepLink: String,
    
    /**
     * Target screen to navigate to
     */
    @field:NotBlank(message = "Target screen is required")
    val targetScreen: String,
    
    /**
     * Whether authentication is required for the target screen
     */
    val requiresAuth: Boolean = true,
    
    /**
     * Additional parameters for the deep link
     */
    val parameters: Map<String, String> = emptyMap()
) 