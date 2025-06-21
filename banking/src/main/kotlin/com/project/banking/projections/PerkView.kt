package com.project.banking.projections

import java.math.BigDecimal

interface PerkView {
    val id: Long
    val type: String
    val minPayment: BigDecimal
    val rewardsXp: Long
    val perkAmount: BigDecimal
    val isTierBased: Boolean
    val categories: List<CategoryView>
}
