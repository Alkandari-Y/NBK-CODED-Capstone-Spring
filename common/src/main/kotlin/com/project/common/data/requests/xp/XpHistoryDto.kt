package com.project.common.data.requests.xp

import com.project.common.enums.XpGainMethod

data class XpHistoryDto(
    val id: Long,
    val amount: Long,
    val gainMethod: XpGainMethod,
    val transactionId: Long,
    val categoryId: Long,
    val recommendationId: Long? = null,
    val promotionId: Long? = null,
    val xpTierId: Long,
    val userXpId: Long,
    val accountId: Long,
    val accountProductId: Long
)