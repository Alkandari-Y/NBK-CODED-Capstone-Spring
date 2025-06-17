package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "event_categories")
data class PromotionCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long? = null,

    @Column(name = "promotion_id", nullable = false)
    var promotionId: Long? = null,
)