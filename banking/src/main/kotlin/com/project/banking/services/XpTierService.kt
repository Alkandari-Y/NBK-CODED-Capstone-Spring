package com.project.banking.services

import com.project.banking.entities.XpTierEntity

interface XpTierService {
    fun getAllTiers(): List<XpTierEntity>
    fun getXpTierById(id: Long): XpTierEntity?
    fun createXpTier(tier: XpTierEntity): XpTierEntity
    fun deleteXpTierById(id: Long)
}