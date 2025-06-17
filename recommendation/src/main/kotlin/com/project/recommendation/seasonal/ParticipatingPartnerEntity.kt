package com.project.recommendation.seasonal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "participating_partners")
data class ParticipatingPartnerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    var id: Long?,

    @Column(name = "partner_id", nullable = false)
    var partnerId: Long?,

    @Column(name = "seasonal_event_id", nullable = false)
    var seasonalEventId: Long?
)