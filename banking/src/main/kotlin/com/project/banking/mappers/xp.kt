package com.project.banking.mappers

import com.project.banking.entities.UserXpEntity
import com.project.banking.entities.XpHistoryEntity
import com.project.banking.entities.XpTierEntity
import com.project.common.data.requests.xp.CreateXpTierRequest
import com.project.common.data.requests.xp.XpHistoryDto
import com.project.common.data.responses.xp.UserXpInfoResponse
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

fun XpTierResponse.toEntity() = XpTierEntity(
    id = id,
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

fun XpHistoryEntity.toDto() = XpHistoryDto(
    amount = amount!!,
    gainMethod = gainMethod!!,
    transactionId = transaction!!.id!!,
    categoryId = category!!.id!!,
    recommendationId = recommendationId,
    promotionId = promotionId,
    xpTierId = xpTier!!.id!!,
    userXpId = userXp!!.id!!,
    accountId = account!!.id!!,
    accountProductId = accountProduct!!.id!!
)

fun UserXpInfoResponse.toEntity(userId: Long) = UserXpEntity(
    id = id,
    userId = userId,
    amount = userXpAmount
)

fun UserXpEntity.toResponse(tier: XpTierResponse) = UserXpInfoResponse(
    id = id!!,
    userXpAmount = amount,
    xpTier = tier
)