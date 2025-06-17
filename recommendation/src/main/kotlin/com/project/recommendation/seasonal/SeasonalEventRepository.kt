package com.project.recommendation.seasonal

import org.springframework.data.jpa.repository.JpaRepository

interface SeasonalEventRepository : JpaRepository<SeasonalEventEntity, Long>
