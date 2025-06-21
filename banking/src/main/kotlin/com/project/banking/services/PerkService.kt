package com.project.banking.services

import com.project.banking.entities.PerkEntity
import com.project.common.data.requests.perks.CreatePerkRequest
import com.project.common.data.requests.perks.PerkCategoryRequest
import com.project.common.data.responses.perks.PerkCategoryResponse
import com.project.common.data.responses.perks.CreatePerkResponse
import com.project.common.data.responses.perks.PerkDto

interface PerkService {
    fun createPerk(productId: Long, request: CreatePerkRequest): CreatePerkResponse
    fun assignPerkCategory(perkId: Long, request: PerkCategoryRequest): PerkCategoryResponse
    fun getPerkById(id: Long): PerkDto?
    fun getAllPerksByAccountProduct(productId: Long): List<PerkDto>
//    fun getAllPerksByBusinessPartner(businessId: Long): List<PerkDto>
}