package com.project.banking.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.project.common.enums.TransactionType
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id", nullable = false)
    @JsonBackReference
    val sourceAccount: AccountEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id", nullable = false)
    @JsonBackReference
    val destinationAccount: AccountEntity?,

    @Column(name = "amount", nullable = false, precision = 9, scale = 3)
    var amount: BigDecimal? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: CategoryEntity? = null,

    @Column(name = "type", nullable = false)
    var transactionType: TransactionType = TransactionType.TRANSFER,
)
