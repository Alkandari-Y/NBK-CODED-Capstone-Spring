package com.project.recommendation.repositories

import com.project.recommendation.entities.FavCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FavCategoryRepository : JpaRepository<FavCategoryEntity, Long>
