package com.project.banking.mappers

import com.project.banking.entities.AccountEntity
import com.project.common.data.requests.accounts.AccountCreateRequest
import com.project.common.data.requests.accounts.AccountResponse
import com.project.common.data.responses.accounts.AccountDto
import java.math.RoundingMode

fun AccountCreateRequest.toEntity(): AccountEntity {
    return AccountEntity(
        name = name,
        balance = initialBalance.setScale(3, RoundingMode.HALF_UP),
    )
}

fun AccountEntity.toBasicResponse() = AccountResponse(
    id = id!!,
    accountNumber = accountNumber,
    name = name,
    balance = balance,
    ownerId = ownerId!!,
    active = active,
    accountType = accountType
)

fun AccountEntity.toDto() = AccountDto(
    accountNumber = this.accountNumber,
    accountType = this.accountType,
    id = this.id!!,
    name = this.name,
    balance = this.balance,
    active = this.active,
    ownerId = this.ownerId!!,
)