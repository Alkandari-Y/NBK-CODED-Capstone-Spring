package com.project.banking.repositories

import com.project.banking.entities.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<CategoryEntity, Long> {
    fun findByName(name: String): CategoryEntity?
}