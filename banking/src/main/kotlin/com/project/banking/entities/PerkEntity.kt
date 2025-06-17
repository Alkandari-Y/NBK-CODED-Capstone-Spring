package com.project.banking.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal


@Entity
@Table(name = "perks")
data class PerkEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null,

    @Column(name = "type", nullable = false)
     var type: Long? = null,

    @Column(name = "min_payment", nullable = false, precision = 8, scale = 2)
     var minPayment: BigDecimal? = null,

    @Column(name = "rewards_xp", nullable = false)
     var rewardsXp: Long? = null,

    @Column(name = "perk_amount", nullable = false, precision = 8, scale = 2)
     var perkAmount: BigDecimal? = null,

    @Column(name = "is_tier_based", nullable = false)
     var isTierBased: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_product_id", nullable = false)
    var accountProduct: AccountProductEntity? = null,
)