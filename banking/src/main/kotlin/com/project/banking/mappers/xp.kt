package com.project.banking.mappers

import com.project.banking.entities.XpTierEntity
import com.project.common.data.requests.xp.CreateXpTierRequest
import com.project.common.data.responses.xp.XpTierResponse

fun CreateXpTierRequest.toEntity() = XpTierEntity(
    minXp = minXp,
    maxXp = maxXp,
    name = name,
    xpPerkMultiplier = xpPerkMultiplier,
    xpPerNotification = xpPerNotification,
    xpPerPromotion = xpPerPromotion,
    perkAmountPercentage = perkAmountPercentage
)

fun XpTierEntity.toResponse() = XpTierResponse(
    id = id!!,
    minXp = minXp!!,
    maxXp = maxXp!!,
    name = name!!,
    xpPerkMultiplier = xpPerkMultiplier!!,
    xpPerNotification = xpPerNotification!!,
    xpPerPromotion = xpPerPromotion!!,
    perkAmountPercentage = perkAmountPercentage!!
)