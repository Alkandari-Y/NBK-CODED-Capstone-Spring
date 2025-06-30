package com.project.recommendation.repositories

import com.project.common.enums.RecommendationType
import com.project.recommendation.entities.RecommendationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RecommendationRepository : JpaRepository<RecommendationEntity, Long> {

    fun findAllByUserIdAndRecType(userId: Long, recType: RecommendationType): List<RecommendationEntity>

    fun deleteAllByUserIdAndRecType(userId: Long, recType: RecommendationType)

    @Modifying
    @Query("DELETE FROM RecommendationEntity r WHERE r.id IN :ids")
    fun deleteAllByIds(@Param("ids") ids: List<Long>)

}
