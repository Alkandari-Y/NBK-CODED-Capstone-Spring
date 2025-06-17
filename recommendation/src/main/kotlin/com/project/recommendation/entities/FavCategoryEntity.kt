package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "fav_categories")
data class FavCategoryEntity(
    @Column(name = "id", nullable = true)
    var id: Long?,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long
)