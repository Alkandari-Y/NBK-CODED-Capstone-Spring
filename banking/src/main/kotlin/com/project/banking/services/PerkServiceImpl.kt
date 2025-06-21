package com.project.banking.services

import com.project.banking.entities.PerkCategoryEntity
import com.project.banking.entities.PerkEntity
import com.project.banking.mappers.toDto
import com.project.banking.mappers.toEntity
import com.project.banking.mappers.toResponse
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.banking.repositories.CategoryRepository
import com.project.banking.repositories.PerkCategoryRepository
import com.project.banking.repositories.PerkRepository
import com.project.common.data.requests.perks.CreatePerkRequest
import com.project.common.data.requests.perks.PerkCategoryRequest
import com.project.common.data.responses.perks.PerkCategoryResponse
import com.project.common.data.responses.perks.CreatePerkResponse
import com.project.common.data.responses.perks.PerkDto
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import com.project.common.exceptions.businessPartner.BusinessNotFoundException
import com.project.common.exceptions.categories.CategoryNotFoundException
import com.project.common.exceptions.perk.NoPerksFoundException
import com.project.common.exceptions.perk.PerkNotFoundException
import com.project.common.exceptions.xp.XPBelowMinException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PerkServiceImpl(
    private val accountProductRepository: AccountProductRepository,
    private val businessPartnerRepository: BusinessPartnerRepository,
    private val categoryRepository: CategoryRepository,
    private val perkRepository: PerkRepository,
    private val perkCategoryRepository: PerkCategoryRepository,
): PerkService {
    @Transactional
    override fun createPerk(productId: Long, request: CreatePerkRequest): CreatePerkResponse {
        val accountProduct = accountProductRepository.findByIdOrNull(productId)
            ?: throw AccountProductNotFoundException()
        if (request.rewardsXp <= 0) { throw XPBelowMinException() }

        val perk = request.toEntity(accountProduct)

        return perkRepository.save(perk).toResponse()
    }

    override fun assignPerkCategory(perkId: Long, request: PerkCategoryRequest): PerkCategoryResponse {
        val perk = perkRepository.findByIdOrNull(perkId)
            ?: throw PerkNotFoundException()
        val category = categoryRepository.findByName(request.categoryName)
            ?: throw CategoryNotFoundException()

        val perkCategory = PerkCategoryEntity(
            perk = perk,
            category = category
        )

        val saved = perkCategoryRepository.save(perkCategory)

        return PerkCategoryResponse(
            perkId = saved.perk!!.id!!,
            categoryId = saved.category!!.id!!,
            categoryName = saved.category!!.name!!
        )
    }

    override fun getPerkById(id: Long): PerkDto? {
        return perkRepository.findByIdOrNull(id)?.toDto()
    }

    override fun getAllPerksByAccountProduct(productId: Long): List<PerkDto> {
        val accountProduct = accountProductRepository.findByIdOrNull(productId)
            ?: throw AccountProductNotFoundException()

        val perks = perkRepository.findAllByAccountProductId(accountProduct.id!!)
            ?: throw NoPerksFoundException()

        return perks.map { it.toDto() }
    }

//    override fun getAllPerksByBusinessPartner(businessId: Long): List<PerkDto> {
//        val businessPartner = businessPartnerRepository.findByIdOrNull(businessId)
//            ?: throw BusinessNotFoundException()
//
//        val categoryId = businessPartner.category?.id
//            ?: throw CategoryNotFoundException("Category not found for ${businessPartner.name}")
//
//        val perks = perkRepository.findAllByCategoryId(categoryId)
//
//        return perks.map { it.toDto() }
//    }
}