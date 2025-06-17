package com.project.recommendation.repositories

import com.project.recommendation.entities.FavBusinessEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FavBusinessRepository : JpaRepository<FavBusinessEntity, Long>
