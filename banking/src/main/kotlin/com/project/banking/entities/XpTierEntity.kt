package com.project.banking.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "xp_tiers")

data class XpTierEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null,

    @Column(name = "min_xp", nullable = false)
     var minXp: Long? = null,

    @Column(name = "max_xp", nullable = false)
     var maxXp: Long? = null,

    @Column(name = "name", nullable = false)
     var name: String? = null,

    @Column(name = "xp_perk_multiplier", nullable = false)
     var xpPerkMultiplier: Long? = null,

    @Column(name = "xp_per_notifcation", nullable = false)
     var xpPerNotifcation: Long? = null,

    @Column(name = "xp_per_event", nullable = false)
     var xpPerEvent: Long? = null,

    @Column(name = "perk_amount_percentage", nullable = false)
     var perkAmountPercentage: Long? = null,
)