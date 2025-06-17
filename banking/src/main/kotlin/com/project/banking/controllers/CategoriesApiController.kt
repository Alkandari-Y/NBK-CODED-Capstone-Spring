package com.project.banking.controllers

import com.project.banking.entities.CategoryEntity
import com.project.banking.mappers.toEntity
import com.project.banking.services.CategoryService
import com.project.common.data.requests.categories.CategoryRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
class CategoriesApiController (
    private val categoryService: CategoryService
){

    @GetMapping
    fun getAllCategories()
    : ResponseEntity<List<CategoryEntity>> = ResponseEntity(
        categoryService.getCategories(),
        HttpStatus.OK
    )

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun createNewCategory(
        @Valid @RequestBody newCategory: CategoryRequest
    ): ResponseEntity<CategoryEntity> {
        val category = categoryService.createCategory(newCategory.toEntity())
        return ResponseEntity(category, HttpStatus.CREATED)
    }
}