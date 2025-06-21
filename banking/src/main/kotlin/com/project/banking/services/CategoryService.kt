package com.project.banking.services

import com.project.banking.entities.CategoryEntity
import com.project.banking.repositories.CategoryWithPerksView

interface CategoryService {
    fun getCategories(): List<CategoryWithPerksView>
    fun createCategory(category: CategoryEntity): CategoryEntity
    fun getCategoryByName(name: String): CategoryEntity?

}