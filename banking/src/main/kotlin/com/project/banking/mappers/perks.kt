package com.project.banking.mappers

import com.project.banking.entities.PerkEntity
import com.project.common.data.responses.perks.CreatePerkResponse
import com.project.common.data.responses.perks.PerkDto

fun PerkEntity.toResponse() = CreatePerkResponse(
    id = id!!,
    type = type!!,
    isTierBased = isTierBased,
    rewardsXp = rewardsXp!!,
    perkAmount = perkAmount!!,
    minPayment = minPayment,
    accountProductId = accountProduct!!.id!!
)

fun PerkEntity.toDto() = PerkDto(
    id = id!!,
    type = type!!,
    isTierBased = isTierBased,
    rewardsXp = rewardsXp!!,
    perkAmount = perkAmount!!,
    minPayment = minPayment,
    accountProductId = accountProduct!!.id!!,
    categories = categories.map { it.toDto() }
)