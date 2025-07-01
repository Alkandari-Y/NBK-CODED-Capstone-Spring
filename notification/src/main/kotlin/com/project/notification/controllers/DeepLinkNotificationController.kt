package com.project.notification.controllers

import com.project.notification.services.DeepLinkNotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for sending promotion and card product deep link notifications via Firebase Cloud Messaging
 */
@RestController
@RequestMapping("/api/v1/notifications/deeplink")
class DeepLinkNotificationController(
    private val deepLinkNotificationService: DeepLinkNotificationService
) {

    /**
     * Send promotion deep link notification to a specific user
     */
    @PostMapping("/promotion")
    fun sendPromotionDeepLinkNotification(
        @RequestBody request: Map<String, Any>
    ): ResponseEntity<Unit> {
        val userId = (request["userId"] as Number).toLong()
        val promotionId = request["promotionId"] as String
        val title = request["title"] as String
        val message = request["message"] as String

        deepLinkNotificationService.sendPromotionDeepLinkNotification(
            userId = userId,
            promotionId = promotionId,
            promotionTitle = title,
            promotionMessage = message
        )
        return ResponseEntity.ok().build()
    }

    /**
     * Send card product deep link notification to a specific user
     */
    @PostMapping("/card-product")
    fun sendCardProductDeepLinkNotification(
        @RequestBody request: Map<String, Any>
    ): ResponseEntity<Unit> {
        val userId = (request["userId"] as Number).toLong()
        val cardProductId = request["cardProductId"] as String
        val title = request["title"] as String
        val message = request["message"] as String

        deepLinkNotificationService.sendCardProductDeepLinkNotification(
            userId = userId,
            cardProductId = cardProductId,
            cardProductTitle = title,
            cardProductMessage = message
        )
        return ResponseEntity.ok().build()
    }

    /**
     * Send promotion deep link notification to multiple users
     */
    @PostMapping("/promotion/bulk")
    fun sendPromotionDeepLinkNotificationToMultipleUsers(
        @RequestBody request: Map<String, Any>
    ): ResponseEntity<Unit> {
        val userIds = (request["userIds"] as List<*>).map { (it as Number).toLong() }
        val promotionId = request["promotionId"] as String
        val title = request["title"] as String
        val message = request["message"] as String

        deepLinkNotificationService.sendPromotionDeepLinkNotificationToMultipleUsers(
            userIds = userIds,
            promotionId = promotionId,
            promotionTitle = title,
            promotionMessage = message
        )
        return ResponseEntity.ok().build()
    }

    /**
     * Send card product deep link notification to multiple users
     */
    @PostMapping("/card-product/bulk")
    fun sendCardProductDeepLinkNotificationToMultipleUsers(
        @RequestBody request: Map<String, Any>
    ): ResponseEntity<Unit> {
        val userIds = (request["userIds"] as List<*>).map { (it as Number).toLong() }
        val cardProductId = request["cardProductId"] as String
        val title = request["title"] as String
        val message = request["message"] as String

        deepLinkNotificationService.sendCardProductDeepLinkNotificationToMultipleUsers(
            userIds = userIds,
            cardProductId = cardProductId,
            cardProductTitle = title,
            cardProductMessage = message
        )
        return ResponseEntity.ok().build()
    }
} 