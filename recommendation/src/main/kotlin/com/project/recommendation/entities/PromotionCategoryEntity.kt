package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "event_categories")
data class PromotionCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long?,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long,

    @Column(name = "promotion_id", nullable = false)
    var promotionId: Long
)