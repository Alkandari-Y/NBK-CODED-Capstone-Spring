package com.project.banking.services

import com.project.banking.entities.CategoryEntity
import com.project.banking.repositories.CategoryRepository
import com.project.banking.repositories.CategoryWithPerksDto
import com.project.banking.repositories.CategoryWithPerksView
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
): CategoryService {
    override fun getCategories(): List<CategoryWithPerksView> {
        return categoryRepository.findAllWithPerkAssociation()
    }

    override fun createCategory(category: CategoryEntity): CategoryEntity {
        return categoryRepository.save(category)
    }

    override fun getCategoryByName(name: String): CategoryEntity? {
        return categoryRepository.findByName(name)
    }
}