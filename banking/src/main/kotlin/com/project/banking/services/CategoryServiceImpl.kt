package com.project.banking.services

import com.project.banking.entities.CategoryEntity
import com.project.banking.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
): CategoryService {
    override fun getCategories(): List<CategoryEntity> {
        return categoryRepository.findAll()
    }

    override fun createCategory(category: CategoryEntity): CategoryEntity {
        return categoryRepository.save(category)
    }

    override fun getCategoryByName(name: String): CategoryEntity? {
        return categoryRepository.findByName(name)
    }
}