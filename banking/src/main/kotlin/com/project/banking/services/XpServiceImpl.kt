package com.project.banking.services

import com.project.banking.entities.UserXpEntity
import com.project.banking.repositories.UserXpRepository
import org.springframework.stereotype.Service

@Service
class XpServiceImpl(
    private val userXpRepository: UserXpRepository
) : XpService {
    override fun userXpInit(userId: Long) {
        val entity = UserXpEntity(userId = userId, amount = 0)
        userXpRepository.save(entity)
    }
}