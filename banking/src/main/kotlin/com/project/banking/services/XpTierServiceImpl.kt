package com.project.banking.services

import com.project.banking.mappers.toEntity
import com.project.banking.mappers.toResponse
import com.project.banking.repositories.XpTierRepository
import com.project.common.data.requests.xp.CreateXpTierRequest
import com.project.common.data.responses.xp.XpTierResponse
import com.project.common.exceptions.xp.XpMinMaxException
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

    override fun getXpTierById(id: Long): XpTierResponse {
        return xpTierRepository.findByIdOrNull(id)?.toResponse()
            ?: throw XpTierNotFoundException()
    }

    override fun createXpTier(tier: CreateXpTierRequest): XpTierResponse {
        if (tier.minXp > tier.maxXp) { throw XpMinMaxException() }

        if (xpTierRepository.existsByName(tier.name)) { throw XpTierNameTakenException() }

        val existingTiers = xpTierRepository.findAllByOrderByMinXpAsc()

        val overlaps = existingTiers.any {
            (tier.minXp in it.minXp!!..it.maxXp!!) ||
            (tier.maxXp in it.minXp!!..it.maxXp!!) ||
            (it.minXp!! in tier.minXp..tier.maxXp) ||
            (it.maxXp!! in tier.minXp..tier.maxXp) }
        if (overlaps) { throw XpMinMaxException("XP tier range overlaps with an existing tier") }

        val combined = existingTiers + tier.toEntity()
        val sorted = combined.sortedBy { it.minXp }

        if (sorted.first().minXp != 0L) {
            throw XpMinMaxException("First tier must start at 0 XP")
        }

        for (i in 0 until sorted.size - 1) {
            val current = sorted[i]
            val next = sorted[i + 1]
            if (current.maxXp!! + 1 != next.minXp!!) {
                throw XpMinMaxException("Gap detected between ${current.name} and ${next.name}")
            }
        }

        val newTier = tier.toEntity()
        return xpTierRepository.save(newTier).toResponse()
    }

    override fun deleteXpTierById(id: Long) {
        val tier = xpTierRepository.findByIdOrNull(id) ?: throw XpTierNotFoundException()
        xpTierRepository.delete(tier)
    }
}