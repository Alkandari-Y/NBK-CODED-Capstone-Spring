package com.project.common.data.responses.xp

data class UserXpInfoResponse(
    val id: Long,
    val userXpAmount: Long,
    val xpTier: XpTierResponse?
)