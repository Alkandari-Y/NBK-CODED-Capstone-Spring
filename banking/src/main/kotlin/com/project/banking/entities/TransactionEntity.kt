package com.project.banking.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.project.common.enums.TransactionType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account", nullable = false)
    @JsonBackReference
    val sourceAccount: AccountEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account", nullable = false)
    @JsonBackReference
    val destinationAccount: AccountEntity?,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    val type: TransactionType? = TransactionType.PAYMENT,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: CategoryEntity? = null,
    ) {
    constructor() : this(
        id = null,
        sourceAccount = null,
        destinationAccount = null,
        amount = BigDecimal.ZERO,
        createdAt = Instant.now(),
        category = null,
        type = TransactionType.TRANSFER
    )
}

