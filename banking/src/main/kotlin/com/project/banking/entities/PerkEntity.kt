package com.project.banking.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.project.common.enums.RewardType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal


@Entity
@Table(name = "perks")
data class PerkEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "type", nullable = false)
    var type: RewardType? = null,

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
    @JsonBackReference
    var accountProduct: AccountProductEntity? = null,

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "perk_categories",
        joinColumns = [JoinColumn(name = "perk_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    @JsonBackReference
    var categories: MutableList<CategoryEntity> = mutableListOf()
)