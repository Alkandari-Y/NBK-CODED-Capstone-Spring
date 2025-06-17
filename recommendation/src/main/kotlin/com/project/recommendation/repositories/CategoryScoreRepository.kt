package com.project.recommendation.repositories

import com.project.recommendation.entities.CategoryScoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryScoreRepository : JpaRepository<CategoryScoreEntity, Long>
