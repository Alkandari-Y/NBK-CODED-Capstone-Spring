package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "fav_businesses")
data class FavBusinessEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @Column(name = "partner_id", nullable = false)
    var partnerId: Long? = null,
)