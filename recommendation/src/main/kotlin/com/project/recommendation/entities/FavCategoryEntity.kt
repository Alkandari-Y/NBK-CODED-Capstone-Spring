package com.project.recommendation.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "fav_categories",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "category_id"])]
)
data class FavCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)