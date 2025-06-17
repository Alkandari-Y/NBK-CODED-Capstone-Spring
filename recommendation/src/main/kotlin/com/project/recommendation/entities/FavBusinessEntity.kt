package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "fav_businesses")
data class FavBusinessEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long?,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "partner_id", nullable = false)
    var partnerId: Long
)