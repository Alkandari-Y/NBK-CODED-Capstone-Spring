package com.project.common.data.responses.accounts

import com.project.common.data.responses.accountProducts.AccountProductDto
import com.project.common.enums.AccountOwnerType
import com.project.common.enums.AccountType
import java.math.BigDecimal

data class AccountWithProductResponse(
    val id: Long,
    val accountNumber: String,
    val balance: BigDecimal,
    val ownerId: Long,
    val ownerType: AccountOwnerType,
    val accountProduct: AccountProductDto,
    val accountType: AccountType,
)