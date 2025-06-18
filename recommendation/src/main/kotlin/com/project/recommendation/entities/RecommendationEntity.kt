package com.project.recommendation.entities

import com.project.common.enums.RecommendationType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "recommendations")
data class RecommendationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long? = null,

    @Column(name = "promotion_id", nullable = true)
    var promotionId: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @Column(name = "partner_id", nullable = true)
    var partnerId: Long? = null,

    @Column(name = "rec_type", nullable = false)
    var recType: RecommendationType? = null,

    @Column(name = "product_id", nullable = true)
    var productId: Long? = null,

    @Column(name = "created_at", nullable = true)
    var createdAt: LocalDateTime = LocalDateTime.now(),
)