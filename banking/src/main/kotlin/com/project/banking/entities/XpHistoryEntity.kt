package com.project.banking.entities

import com.project.common.enums.XpGainMethod
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "xp_history")
data class XpHistoryEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null,

    @Column(name = "amount", nullable = false)
     var amount: Long? = null,

    @Column(name = "gain_method", nullable = false)
     var gainMethod: XpGainMethod? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
     var transaction: TransactionEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
     var category: CategoryEntity? = null,

    @Column(name = "recommendation_id")
     var recommendationId: Long? = null,

    @Column(name = "promotion_id")
     var promotionId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xp_tier_id", nullable = false)
     var xpTier: XpTierEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_xp_id")
     var userXp: UserXpEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
     var account: AccountEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_product_id", nullable = false)
     var accountProduct: AccountProductEntity? = null,
)