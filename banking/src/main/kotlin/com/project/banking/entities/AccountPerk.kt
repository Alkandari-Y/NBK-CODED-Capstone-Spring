package com.project.banking.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.math.BigDecimal


@Entity
@Table(name = "account_perks")
@Data
@NoArgsConstructor
@AllArgsConstructor
class AccountPerkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "account_product_id", nullable = false)
    var accountProductId: Long? = null

    @Column(name = "perk_id", nullable = false)
    var perkId: Long? = null
}