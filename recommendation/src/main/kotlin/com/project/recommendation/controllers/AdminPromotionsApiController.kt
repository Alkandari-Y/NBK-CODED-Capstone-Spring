package com.project.recommendation.controllers

import com.project.common.data.requests.promotions.CreatePromotionRequest
import com.project.common.data.responses.promotions.PromotionResponse
import com.project.recommendation.services.PromotionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/promotions")
class AdminPromotionsApiController(
    private val promotionService: PromotionService
) {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun createPromotion(@Valid @RequestBody request: CreatePromotionRequest): ResponseEntity<PromotionResponse> {
        val body = promotionService.createPromotion(request)
        return ResponseEntity(body, HttpStatus.CREATED)
    }
}