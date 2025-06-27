package com.project.common.data.responses.accountProducts

import com.project.common.data.responses.perks.PerkDto
import com.project.common.enums.AccountType

data class AccountProductSummaryDto(
    val id: Long,
    val name: String,
    val accountType: AccountType,
    val perkIds: List<Long> = emptyList(),
    val categoryIds: List<Long> = emptyList(),
)