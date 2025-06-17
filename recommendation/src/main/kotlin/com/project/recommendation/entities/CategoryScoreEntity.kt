package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "category_scores")
data class CategoryScoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long?,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "frequency", nullable = false)
    var frequency: Long,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long
)
