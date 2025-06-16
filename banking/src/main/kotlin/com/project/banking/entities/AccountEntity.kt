package com.project.banking.entities


import com.project.common.data.requests.accounts.AccountResponse
import com.project.common.enums.AccountOwnerType
import com.project.common.enums.AccountType
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.math.BigDecimal
import java.util.*


@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null

    @Column(name = "name", nullable = false)
     var name: String? = null

    @Column(name = "balance", nullable = false, precision = 9, scale = 3)
     var balance: BigDecimal? = null

    @Column(name = "is_active", nullable = false)
     var isActive = false

    @Column(name = "account_number", nullable = false, unique = true)
     var accountNumber: String? = null

    @Column(name = "owner_id", nullable = false)
     var ownerId: Long? = null

    @Column(name = "owner_type", nullable = false)
     var ownerType: AccountOwnerType = AccountOwnerType.CLIENT

    @Column(name = "account_product_id", nullable = false)
     var accountProductId: Long? = null

    @Column(name = "account_type", nullable = false)
     var accountType: AccountType? = null
}

fun AccountEntity.toAccountResponseDto() = AccountResponse(
    id = this.id!!,
    accountNumber = this.accountNumber!!,
    name = this.name!!,
    balance = this.balance!!,
    active = this.isActive,
    ownerId = this.ownerId!!,
    accountType = this.accountType!!
)
