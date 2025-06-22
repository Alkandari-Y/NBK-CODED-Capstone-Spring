package com.project.banking.services

import com.project.common.data.responses.xp.XpTierResponse

interface XpService {
    fun userXpInit(userId: Long)
    fun earnXP(userId: Long, amount: Long)
    fun getCurrentTier(userId: Long): XpTierResponse
}