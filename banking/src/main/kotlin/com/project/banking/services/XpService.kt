package com.project.banking.services

import com.project.banking.entities.XpHistoryEntity
import com.project.common.data.responses.xp.UserXpInfoResponse

interface XpService {
    fun userXpInit(userId: Long)
    fun earnXP(historyEntity: XpHistoryEntity)
    fun getCurrentXpInfo(userId: Long): UserXpInfoResponse?
}