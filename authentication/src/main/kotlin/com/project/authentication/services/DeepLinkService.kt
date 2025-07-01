package com.project.authentication.services

import com.project.common.data.requests.deeplink.DeepLinkRequest
import com.project.common.data.responses.deeplink.DeepLinkResponse
import com.project.common.exceptions.APIException
import org.springframework.stereotype.Service
import java.net.URI

/**
 * Service for handling deep link business logic
 */
@Service
class DeepLinkService {

    /**
     * Process deep link and return navigation information
     */
    fun processDeepLink(request: DeepLinkRequest): DeepLinkResponse {
        val deepLink = request.deepLink ?: throw APIException("Deep link is required")
        
        if (!deepLink.startsWith("nbkcapstone://")) {
            throw APIException("Invalid deep link scheme. Expected 'nbkcapstone://'")
        }

        val uri = URI(deepLink)
        val targetScreen = uri.host
        val pathSegments = uri.path?.split("/")?.filter { it.isNotEmpty() } ?: emptyList()
        
        return when (targetScreen) {
            // Public screens (no auth required)
            "login" -> DeepLinkResponse(
                targetScreen = "login",
                requiresAuth = false,
                parameters = emptyMap(),
                message = "Navigate to login screen"
            )
            
            "signup" -> DeepLinkResponse(
                targetScreen = "signup",
                requiresAuth = false,
                parameters = emptyMap(),
                message = "Navigate to signup screen"
            )
            
            // Protected screens (auth required)
            "wallet" -> DeepLinkResponse(
                targetScreen = "wallet",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Navigate to wallet screen"
            )
            
            "transfer" -> DeepLinkResponse(
                targetScreen = "transfer",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Navigate to transfer screen"
            )
            
            "calendar" -> DeepLinkResponse(
                targetScreen = "calendar",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Navigate to calendar screen"
            )
            
            "recommendations" -> DeepLinkResponse(
                targetScreen = "recommendations",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Navigate to recommendations screen"
            )
            
            "home" -> DeepLinkResponse(
                targetScreen = "home",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Navigate to home screen"
            )
            
            "profile" -> DeepLinkResponse(
                targetScreen = "profile",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Navigate to profile screen"
            )
            
            "xp" -> DeepLinkResponse(
                targetScreen = "xp",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Navigate to XP history screen"
            )
            
            "notifications" -> DeepLinkResponse(
                targetScreen = "notifications",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Navigate to notifications screen"
            )
            
            "promotion" -> {
                val promotionId = pathSegments.firstOrNull()
                if (promotionId != null) {
                    DeepLinkResponse(
                        targetScreen = "promotion",
                        requiresAuth = true,
                        parameters = mapOf("promotionId" to promotionId),
                        message = "Navigate to promotion details screen"
                    )
                } else {
                    throw APIException("Promotion ID is required for promotion deep link")
                }
            }
            
            "card-product" -> {
                val cardProductId = pathSegments.firstOrNull()
                if (cardProductId != null) {
                    DeepLinkResponse(
                        targetScreen = "card-product",
                        requiresAuth = true,
                        parameters = mapOf("cardProductId" to cardProductId),
                        message = "Navigate to card product details screen"
                    )
                } else {
                    throw APIException("Card Product ID is required for card-product deep link")
                }
            }
            
            else -> DeepLinkResponse(
                targetScreen = "home",
                requiresAuth = true,
                parameters = emptyMap(),
                message = "Default navigation to home screen"
            )
        }
    }

    /**
     * Generate deep link URL for sharing
     */
    fun generateDeepLink(request: DeepLinkRequest): DeepLinkResponse {
        val targetScreen = request.targetScreen ?: throw APIException("Target screen is required")
        val parameters = request.parameters ?: emptyMap()
        
        val deepLink = when (targetScreen) {
            "wallet" -> "nbkcapstone://wallet"
            "transfer" -> "nbkcapstone://transfer"
            "calendar" -> "nbkcapstone://calendar"
            "recommendations" -> "nbkcapstone://recommendations"
            "home" -> "nbkcapstone://home"
            "login" -> "nbkcapstone://login"
            "signup" -> "nbkcapstone://signup"
            "profile" -> "nbkcapstone://profile"
            "xp" -> "nbkcapstone://xp"
            "notifications" -> "nbkcapstone://notifications"
            "promotion" -> {
                val promotionId = parameters["promotionId"]
                if (promotionId != null) {
                    "nbkcapstone://promotion/$promotionId"
                } else {
                    throw APIException("Promotion ID is required for promotion deep link")
                }
            }
            "card-product" -> {
                val cardProductId = parameters["cardProductId"]
                if (cardProductId != null) {
                    "nbkcapstone://card-product/$cardProductId"
                } else {
                    throw APIException("Card Product ID is required for card-product deep link")
                }
            }
            else -> throw APIException("Invalid target screen: $targetScreen")
        }
        
        return DeepLinkResponse(
            targetScreen = targetScreen,
            requiresAuth = targetScreen !in listOf("login", "signup"),
            parameters = parameters,
            message = "Generated deep link",
            deepLink = deepLink
        )
    }

    /**
     * Validate deep link format
     */
    fun validateDeepLink(request: DeepLinkRequest): DeepLinkResponse {
        val deepLink = request.deepLink ?: throw APIException("Deep link is required")
        
        return try {
            if (!deepLink.startsWith("nbkcapstone://")) {
                throw APIException("Invalid deep link scheme")
            }
            
            val uri = URI(deepLink)
            val targetScreen = uri.host
            
            val isValidScreen = targetScreen in listOf(
                "login", "signup", "wallet", "transfer", "calendar", 
                "recommendations", "home", "profile", "xp", "notifications", 
                "promotion", "card-product"
            )
            
            if (!isValidScreen) {
                throw APIException("Invalid target screen: $targetScreen")
            }
            
            DeepLinkResponse(
                targetScreen = targetScreen,
                requiresAuth = targetScreen !in listOf("login", "signup"),
                parameters = emptyMap(),
                message = "Deep link is valid"
            )
            
        } catch (e: Exception) {
            throw APIException("Invalid deep link format: ${e.message}")
        }
    }

    /**
     * Get all supported deep link screens
     */
    fun getSupportedScreens(): List<String> {
        return listOf(
            "login", "signup", "wallet", "transfer", "calendar", 
            "recommendations", "home", "profile", "xp", "notifications", 
            "promotion", "card-product"
        )
    }

    /**
     * Check if a screen requires authentication
     */
    fun requiresAuthentication(screen: String): Boolean {
        return screen !in listOf("login", "signup")
    }
} 