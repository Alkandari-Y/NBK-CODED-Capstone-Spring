package com.project.banking.mappers

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.AccountProductEntity
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.enums.AccountOwnerType
import java.math.RoundingMode

fun AccountCreateRequest.toEntity(accountProduct: AccountProductEntity): AccountEntity {
    return AccountEntity(
        balance = initialBalance.setScale(3, RoundingMode.HALF_UP),

    )
}

fun AccountEntity.toDto() = AccountDto(
    id = this.id!!,
    accountNumber = this.accountNumber,
    balance = this.balance,
    ownerId = this.ownerId!!,
    ownerType = this.ownerType,
    accountProductId = this.accountProduct?.id!!,
    accountType = this.accountType!!
)

fun AccountCreateRequest.toEntity(ownerId: Long, accountProduct: AccountProductEntity) = AccountEntity(
    accountProduct = accountProduct,
    ownerId = ownerId,
    balance = this.initialBalance.setScale(3, RoundingMode.HALF_UP),
    ownerType = AccountOwnerType.CLIENT,
    accountType = accountProduct.accountType
)