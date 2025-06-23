package com.project.banking.services

import com.project.banking.entities.UserXpEntity
import com.project.banking.entities.XpHistoryEntity
import com.project.banking.mappers.toResponse
import com.project.banking.repositories.PerkRepository
import com.project.banking.repositories.UserXpRepository
import com.project.banking.repositories.XpHistoryRepository
import com.project.common.data.requests.xp.XpHistoryDto
import com.project.common.data.responses.xp.UserXpInfoResponse
import com.project.common.exceptions.auth.UserNotFoundException
import com.project.common.exceptions.xp.UserXpInfoNotFoundException
import com.project.common.exceptions.xp.XpTierNotFoundException
import org.springframework.stereotype.Service

@Service
class XpServiceImpl(
    private val userXpRepository: UserXpRepository,
    private val xpHistoryRepository: XpHistoryRepository,
    private val xpTierService: XpTierService
) : XpService {
    override fun userXpInit(userId: Long) {
        val existing = userXpRepository.findByUserId(userId)
        if (existing != null) return

        val entity = UserXpEntity(userId = userId, amount = 0)
        userXpRepository.save(entity)
    }

    override fun earnXP(historyEntity: XpHistoryEntity) {
        val entity = userXpRepository.findByUserId(historyEntity.userXp!!.userId)
            ?: throw UserNotFoundException()

        entity.amount += historyEntity.amount!!

        xpHistoryRepository.save(historyEntity)

        userXpRepository.save(entity)
    }

    override fun getCurrentXpInfo(userId: Long): UserXpInfoResponse? {
        val entity = userXpRepository.findByUserId(userId)
            ?: throw UserXpInfoNotFoundException()
        val tier = xpTierService.getTierByXp(entity.amount)
            ?: throw XpTierNotFoundException()

        return entity.toResponse(tier)
    }
}