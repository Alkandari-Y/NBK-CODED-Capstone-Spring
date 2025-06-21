package com.project.banking.services

import com.project.banking.mappers.toEntity
import com.project.banking.mappers.toResponse
import com.project.banking.repositories.XpTierRepository
import com.project.common.data.requests.xp.CreateXpTierRequest
import com.project.common.data.responses.xp.XpTierResponse
import com.project.common.exceptions.xp.XpTierNameTakenException
import com.project.common.exceptions.xp.XpTierNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class XpTierServiceImpl(
    private val xpTierRepository: XpTierRepository
) : XpTierService {
    override fun getAllTiers(): List<XpTierResponse> {
        val tiers = xpTierRepository.findAll()
        return tiers.map { it.toResponse() }
    }

    override fun getXpTierById(id: Long): XpTierResponse? {
        return xpTierRepository.findByIdOrNull(id)?.toResponse()
            ?: throw XpTierNotFoundException()
    }

    override fun createXpTier(tier: CreateXpTierRequest): XpTierResponse {
        if (tier.minXp > tier.maxXp) {
            throw IllegalArgumentException("minXp cannot be greater than maxXp")
        }

        if (xpTierRepository.findAll().any { it.name == tier.name }) {
            throw XpTierNameTakenException("Tier name must be unique")
        }

        val overlaps = xpTierRepository.findAll().any {
            (tier.minXp in it.minXp!!..it.maxXp!!) ||
            (tier.maxXp in it.minXp!!..it.maxXp!!) ||
            (it.minXp!! in tier.minXp..tier.maxXp) ||
            (it.maxXp!! in tier.minXp..tier.maxXp)
        }
        if (overlaps) {
            throw IllegalArgumentException("Tier range overlaps with an existing tier")
        }

        val newTier = tier.toEntity()
        return xpTierRepository.save(newTier).toResponse()
    }

    override fun deleteXpTierById(id: Long) {
        xpTierRepository.deleteById(id)
    }
}