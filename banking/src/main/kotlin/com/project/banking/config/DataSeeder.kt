package com.project.banking.config

import com.project.banking.entities.CategoryEntity
import com.project.banking.repositories.CategoryRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataSeeder(
    private val categoryRepository: CategoryRepository
) {
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun seedInitialData() {
        if (categoryRepository.count() == 0L) {
            val categories = listOf(
                "personal", "retail", "manufacturing", "healthcare", "financial services",
                "real estate", "technology", "hospitality", "education", "logistics",
                "construction", "agriculture", "automotive", "consulting", "wholesale", "energy"
            ).map { name -> CategoryEntity(name = name) }

            categoryRepository.saveAll(categories)
            println("Seeded default categories.")
        } else {
            println("Categories already exist. Skipping seeding.")
        }
    }
}
