package com.project.recommendation.controllers

import com.project.common.data.responses.promotions.PromotionResponse
import com.project.common.exceptions.promotions.PromotionNotFoundException
import com.project.recommendation.services.PromotionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/promotions")
class PromotionApiController(
    private val promotionService: PromotionService
) {

    @GetMapping("/{id}")
    fun getPromotionById(@PathVariable id: Long): ResponseEntity<PromotionResponse> {
        val body = promotionService.getPromotionById(id) ?: throw PromotionNotFoundException()
        return ResponseEntity(body, HttpStatus.OK)
    }

    @GetMapping("/business/{businessId}")
    fun getPromotionsByBusiness(@PathVariable businessId: Long): ResponseEntity<List<PromotionResponse>> {
        val body = promotionService.getAllPromotionsByBusinessId(businessId)
        return ResponseEntity(body, HttpStatus.OK)
    }
}