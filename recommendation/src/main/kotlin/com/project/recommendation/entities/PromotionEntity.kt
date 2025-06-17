package com.project.recommendation.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "promotions")
data class PromotionEntity(
    @Column(name = "id", nullable = true)
    var id: Long?,

    @Column(name = "business_partner_id", nullable = false)
    var businessPartnerId: Long,

    @Column(name = "type", nullable = false)
    var type: Long,

    @Column(name = "start_date", nullable = true)
    var startDate: LocalDate?,

    @Column(name = "end_date", nullable = true)
    var endDate: LocalDate?,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "store_id", nullable = true)
    var storeId: Long?,

    @Column(name = "xp", nullable = false)
    var xp: Long
)