package com.project.banking.entities

import com.project.common.enums.TransactionType
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null

    @Column(name = "source_account", nullable = false)
     var sourceAccount: String? = null

    @Column(name = "destination_account", nullable = false)
     var destinationAccount: String? = null

    @Column(name = "amount", nullable = false, precision = 9, scale = 3)
     var amount: BigDecimal? = null

    @Column(name = "created_at", nullable = false)
     var createdAt: LocalDateTime? = null

    @Column(name = "category_id", nullable = false)
     var categoryId: Int? = null

    @Column(name = "type", nullable = false)
     var transactionType: TransactionType = TransactionType.TRANSFER
}
