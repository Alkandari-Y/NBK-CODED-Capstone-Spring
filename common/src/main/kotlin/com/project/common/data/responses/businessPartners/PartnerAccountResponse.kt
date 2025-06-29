package com.project.common.data.responses.businessPartners

import com.project.common.data.responses.accounts.AccountDto
import com.project.common.data.responses.categories.CategoryDto

data class PartnerAccountResponse(
    val id: Long,
    val name: String,
    val account: AccountDto,
    val logoUrl: String,
    val category: CategoryDto,
)