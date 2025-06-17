package com.project.banking.entities

import com.project.common.enums.AccountType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal


@Entity
@Table(name = "account_products")
data class AccountProductEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null,

    @Column(name = "name", nullable = false)
     var name: String? = null,

    @Column(name = "type", nullable = false)
     var accountType: AccountType = AccountType.DEBIT,

    @Column(name = "interest_rate", precision = 9, scale = 2)
     var interestRate: BigDecimal? = null,

    @Column(name = "min_balance_required", precision = 9, scale = 2)
     var minBalanceRequired: BigDecimal? = null,

    @Column(name = "credit_limit", precision = 9, scale = 2)
     var creditLimit: BigDecimal? = null,

    @Column(name = "annual_fee", precision = 9, scale = 2)
     var annualFee: BigDecimal? = null,

    @Column(name = "min_salary")
     var minSalary: Long? = null,

    @Column(name = "image")
     var image: String? = null,
)