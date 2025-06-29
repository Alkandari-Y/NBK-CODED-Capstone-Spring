package com.project.banking.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.project.common.enums.AccountType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal


@Entity
@Table(name = "account_products")
data class AccountProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    var name: String? = null,

    @Column(name = "type", nullable = false)
    var accountType: AccountType = AccountType.DEBIT,

    @Column(name = "description", nullable = false)
    var description: String = "",

    @Column(name = "interest_rate", precision = 9, scale = 3)
    var interestRate: BigDecimal = BigDecimal.ZERO,

    @Column(name = "min_balance_required", precision = 9, scale = 3)
    var minBalanceRequired: BigDecimal = BigDecimal.ZERO,

    @Column(name = "credit_limit", precision = 9, scale = 3)
    var creditLimit: BigDecimal = BigDecimal.ZERO,

    @Column(name = "annual_fee", precision = 9, scale = 3)
    var annualFee: BigDecimal = BigDecimal.ZERO,

    @Column(name = "min_salary", precision = 9, scale = 2)
    var minSalary: BigDecimal = BigDecimal.ZERO,

    @Column(name = "image")
    var image: String? = null,

    @OneToMany(
        mappedBy = "accountProduct",
        cascade = [CascadeType.MERGE],
        fetch = FetchType.LAZY
    )
    @JsonManagedReference
    var perks: MutableList<PerkEntity> = mutableListOf()
)