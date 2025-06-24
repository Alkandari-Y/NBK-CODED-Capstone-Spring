package com.project.banking.services

import com.project.banking.entities.XpHistoryEntity
import com.project.common.data.requests.xp.XpHistoryDto
import com.project.common.data.responses.xp.UserXpInfoResponse
import java.time.LocalDateTime

interface XpService {
    fun userXpInit(userId: Long)
    fun earnXP(historyEntity: XpHistoryEntity)
    fun getCurrentXpInfo(userId: Long): UserXpInfoResponse?
    fun getUserXpHistory(userId: Long): List<XpHistoryDto>
    fun countPerkXpEvents(userId: Long, accountProductId: Long, after: LocalDateTime): Int
}