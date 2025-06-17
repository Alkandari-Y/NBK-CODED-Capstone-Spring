package com.project.recommendation.repositories

import com.project.recommendation.entities.AccountScoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountScoreRepository : JpaRepository<AccountScoreEntity, Long>
