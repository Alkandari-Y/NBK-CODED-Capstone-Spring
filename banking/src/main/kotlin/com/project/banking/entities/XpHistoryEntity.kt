package com.project.banking.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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
     var gainMethod: Int? = null,

    @Column(name = "transaction_id")
     var transactionId: Long? = null,

    @Column(name = "category_id", nullable = false)
     var categoryId: Long? = null,

    @Column(name = "recommendation_id")
     var recommendationId: Long? = null,

    @Column(name = "promotion_id")
     var promotionId: Long? = null,

    @Column(name = "xp_tier_id", nullable = false)
     var xpTierId: Long? = null,

    @Column(name = "user_xp_id")
     var userXpId: Long? = null,

    @Column(name = "account_id", nullable = false)
     var accountId: Long? = null,

    @Column(name = "account_product_id", nullable = false)
     var accountProductId: Long? = null,
)