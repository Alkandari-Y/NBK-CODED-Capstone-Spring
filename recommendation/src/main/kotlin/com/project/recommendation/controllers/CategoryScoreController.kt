package com.project.recommendation.controllers

import com.project.common.data.requests.categoryScores.IncrementCategoryScoreRequest
import com.project.common.data.requests.categoryScores.InitializeCategoryScores
import com.project.recommendation.entities.CategoryScoreEntity
import com.project.recommendation.services.CategoryScoreService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/category-scores")
class CategoryScoreController(
    private val categoryScoreService: CategoryScoreService,
) {
    @PostMapping
    fun initializeCategoryScores(@Valid @RequestBody request: InitializeCategoryScores) {
        categoryScoreService.createUserCategoryScores(userId = request.userId)
    }

    @PostMapping("/increment")
    fun incrementCategoryFrequency(@Valid @RequestBody request: IncrementCategoryScoreRequest) {
        categoryScoreService.incrementCategoryFrequency(request)
    }

//    @GetMapping("{id}")
//    fun getCategoryScoreById(userId: Long, categoryId: Long): CategoryScoreEntity {
//        return categoryScoreService.findCategoryScoreById(userId, categoryId)
//    }
}