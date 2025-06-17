package com.project.banking.entities


import com.project.banking.utils.generateAccountNumber
import com.project.common.enums.AccountOwnerType
import com.project.common.enums.AccountType
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "accounts")
data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "balance", nullable = false, precision = 9, scale = 3)
    var balance: BigDecimal = BigDecimal.ZERO,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "account_number", nullable = false, unique = true)
    var accountNumber: String = generateAccountNumber(),

    @Column(name = "owner_id", nullable = false)
    var ownerId: Long? = null,

    @Column(name = "owner_type", nullable = false)
    var ownerType: AccountOwnerType = AccountOwnerType.CLIENT,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_product_id", nullable = false)
    var accountProduct: AccountProductEntity? = null,

    @Column(name = "account_type", nullable = false)
    var accountType: AccountType? = null
)
