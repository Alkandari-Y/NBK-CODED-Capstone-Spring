package com.project.recommendation.entities

import com.project.common.enums.RewardType
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "promotions")
data class PromotionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    var id: Long? = null,

    @Column(name = "name", nullable = true)
    var name: String = "",

    @Column(name = "business_partner_id", nullable = false)
    var businessPartnerId: Long? = null,

    @Column(name = "type", nullable = false)
    var type: RewardType? = RewardType.DISCOUNT,

    @Column(name = "start_date", nullable = true)
    var startDate: LocalDate? = null,

    @Column(name = "end_date", nullable = true)
    var endDate: LocalDate? = null,

    @Column(name = "description", nullable = false)
    var description: String = "",

    @Column(name = "store_id", nullable = true)
    var storeId: Long? = null
)