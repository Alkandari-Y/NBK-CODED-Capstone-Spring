package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "category_scores")
data class CategoryScoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @Column(name = "frequency", nullable = false)
    var frequency: Long = 0L,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long? = null,
)
