package com.project.common.data.responses.recommendation

import com.project.common.enums.RecommendationType
import java.time.LocalDateTime

data class RecommendationDto(
    val id: Long?,
    val genericIdRef: Long?,
    val userId: Long?,
    val recType: RecommendationType?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)