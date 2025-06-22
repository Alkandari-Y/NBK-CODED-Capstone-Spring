package com.project.common.data.responses.xp

data class UserXpInfoResponse(
    val userXpAmount: Long,
    val xpTier: XpTierResponse?
)