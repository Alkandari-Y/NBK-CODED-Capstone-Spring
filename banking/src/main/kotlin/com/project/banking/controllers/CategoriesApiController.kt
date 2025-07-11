package com.project.banking.controllers


import com.project.banking.mappers.toEntity
import com.project.banking.services.CategoryService
import com.project.common.data.requests.categories.CategoryRequest
import com.project.common.data.responses.categories.CategoryDto
import com.project.common.data.responses.categories.CategoryWithPerksDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.text.split

@RestController
@RequestMapping("/api/v1/categories")
class CategoriesApiController (
    private val categoryService: CategoryService
){
    @GetMapping
    fun getAllCategories()
    : ResponseEntity<List<CategoryWithPerksDto>> {
        val categories = categoryService.getCategories().map { cat ->
            CategoryWithPerksDto(
                id = cat.id,
                hasPerks = cat.hasPerks,
                name = cat.name.split(" ")
                .joinToString(" ") { name ->
                    name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                }
            )
        }

        return ResponseEntity(categories, HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun createNewCategory(
        @Valid @RequestBody newCategory: CategoryRequest
    ): ResponseEntity<CategoryDto> {
        val category = categoryService.createCategory(newCategory.toEntity())
        return ResponseEntity(CategoryDto(id=category.id!!, name = category.name!!), HttpStatus.CREATED)
    }
}