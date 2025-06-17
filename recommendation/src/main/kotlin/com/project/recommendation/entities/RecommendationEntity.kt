package com.project.recommendation.entities

import com.project.common.enums.RecommendationType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "recommendations")
data class RecommendationEntity(
    @Column(name = "id", nullable = true)
    var id: Long?,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long,

    @Column(name = "promotion_id", nullable = true)
    var promotionId: Long?,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "partner_id", nullable = true)
    var partnerId: Long?,

    @Column(name = "rec_type", nullable = false)
    var recType: RecommendationType,

    @Column(name = "product_id", nullable = true)
    var productId: Long?,

    @Column(name = "created_at", nullable = true)
    var createdAt: LocalDateTime?
)