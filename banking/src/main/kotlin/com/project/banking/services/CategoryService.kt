package com.project.banking.services

import com.project.banking.entities.CategoryEntity

interface CategoryService {
    fun getCategories(): List<CategoryEntity>
    fun createCategory(category: CategoryEntity): CategoryEntity
    fun getCategoryByName(name: String): CategoryEntity?
}