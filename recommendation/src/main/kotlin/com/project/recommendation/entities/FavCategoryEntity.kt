package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "fav_categories")
data class FavCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long? = null,
)