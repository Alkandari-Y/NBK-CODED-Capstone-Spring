package com.project.banking.entities.projections

import java.math.BigDecimal

interface AccountListItemProjection {
    val id: Long
    val name: String
    val accountNumber: String
    val balance: BigDecimal
    val active: Boolean
    val ownerId: Long
    val isPrimary: Boolean
}
