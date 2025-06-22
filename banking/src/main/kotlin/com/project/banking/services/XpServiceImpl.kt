package com.project.banking.services

import com.project.banking.entities.UserXpEntity
import com.project.banking.repositories.UserXpRepository
import com.project.banking.repositories.XpTierRepository
import com.project.common.data.responses.xp.XpTierResponse
import com.project.common.exceptions.auth.UserNotFoundException
import org.springframework.stereotype.Service

@Service
class XpServiceImpl(
    private val userXpRepository: UserXpRepository,
    private val xpTierService: XpTierService
) : XpService {
    override fun userXpInit(userId: Long) {
        val entity = UserXpEntity(userId = userId, amount = 0)
        userXpRepository.save(entity)
    }

    override fun earnXP(userId: Long, amount: Long) {
        val entity = userXpRepository.findByUserId(userId)
            ?: throw UserNotFoundException()

        entity.amount += amount
        userXpRepository.save(entity)
    }

    override fun getCurrentTier(userId: Long): XpTierResponse {
        val entity = userXpRepository.findByUserId(userId)
            ?: throw UserNotFoundException()

        return xpTierService.getTierByXp(entity.amount)
    }
}