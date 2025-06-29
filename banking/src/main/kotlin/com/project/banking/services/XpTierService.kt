package com.project.banking.services

import com.project.banking.entities.XpTierEntity
import com.project.common.data.requests.xp.CreateXpTierRequest
import com.project.common.data.responses.xp.XpTierResponse
import jdk.jfr.DataAmount

interface XpTierService {
    fun getAllTiers(): List<XpTierResponse>?
    fun getXpTierById(id: Long): XpTierResponse?
    fun getTierByXp(amount: Long): XpTierResponse?
    fun createXpTier(tier: CreateXpTierRequest): XpTierResponse
    fun deleteXpTierById(id: Long)
}