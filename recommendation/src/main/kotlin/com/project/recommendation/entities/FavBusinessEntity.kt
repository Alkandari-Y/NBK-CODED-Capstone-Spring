package com.project.recommendation.entities

import jakarta.persistence.*

@Entity
@Table(name = "fav_businesses")
data class FavBusinessEntity(
    @Column(name = "id", nullable = true)
    var id: Long?,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "partner_id", nullable = false)
    var partnerId: Long
)