package com.project.banking.mappers

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.AccountProductEntity
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.enums.AccountOwnerType
import com.project.common.enums.AccountType
import java.math.BigDecimal

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
    balance = if (accountProduct.accountType == AccountType.DEBIT) BigDecimal.ZERO else accountProduct.creditLimit,
    ownerType = AccountOwnerType.CLIENT,
    accountType = accountProduct.accountType
)