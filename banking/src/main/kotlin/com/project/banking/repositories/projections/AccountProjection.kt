package com.project.banking.repositories.projections

import com.project.common.enums.AccountType
import java.math.BigDecimal

interface AccountView {
    fun getId(): Long
    fun getAccountNumber(): String
    fun getName(): String
    fun getBalance(): BigDecimal
    fun isActive(): Boolean
    fun getOwnerId(): Long
    fun getAccountType(): AccountType
}


