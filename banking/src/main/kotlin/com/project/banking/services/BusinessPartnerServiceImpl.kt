package com.project.banking.services

import com.project.banking.entities.AccountEntity
import com.project.banking.entities.BusinessPartnerEntity
import com.project.banking.mappers.toDto
import com.project.banking.mappers.toEntity
import com.project.banking.repositories.AccountProductRepository
import com.project.banking.repositories.AccountRepository
import com.project.banking.repositories.BusinessPartnerRepository
import com.project.banking.repositories.CategoryRepository
import com.project.common.data.requests.businessPartners.CreateBusinessPartnerRequest
import com.project.common.data.responses.authentication.UserInfoDto
import com.project.common.data.responses.businessPartners.PartnerAccountResponse
import com.project.common.enums.AccountOwnerType
import com.project.common.enums.AccountType
import com.project.common.exceptions.accountProducts.AccountProductNotFoundException
import com.project.common.exceptions.accounts.AccountNotFoundException
import com.project.common.exceptions.businessPartner.BusinessNotFoundException
import com.project.common.exceptions.categories.CategoryNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BusinessServiceImpl(
    private val businessPartnerRepository: BusinessPartnerRepository,
    private val accountProductRepository: AccountProductRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository
): BusinessPartnerService {
    override fun getAllPartners(): List<BusinessPartnerEntity> {
        return businessPartnerRepository.findAll()
    }

    override fun getPartnerById(id: Long): BusinessPartnerEntity? {
        return businessPartnerRepository.findByIdOrNull(id)
    }

    override fun createBusinessPartner(
       businessPartnerRequest: CreateBusinessPartnerRequest,
        userInfoDto: UserInfoDto
    ): BusinessPartnerEntity {

        val category = categoryRepository.findByIdOrNull(businessPartnerRequest.categoryId)
            ?: throw CategoryNotFoundException()
        val accountProduct = accountProductRepository.findByAccountTypeAndName(
            accountType = AccountType.DEBIT,
            name = "Business Account",
        ) ?: throw AccountProductNotFoundException()

        val businessAccount = accountRepository.save(
            AccountEntity(
                isActive = true,
                ownerId = userInfoDto.userId,
                ownerType = AccountOwnerType.BUSINESS,
                accountProduct = accountProduct,
                accountType = AccountType.DEBIT
            ))

        val newBusiness = businessPartnerRepository.save(
            businessPartnerRequest.toEntity(
                businessAccount = businessAccount,
                category = category,
                userId = userInfoDto.userId,
            ))

        return newBusiness
    }

    override fun deletePartnerById(id: Long) {
        val business = businessPartnerRepository.findByIdOrNull(id)
            ?: throw BusinessNotFoundException()
        accountRepository.deleteById(business.account?.id!!)
        businessPartnerRepository.deleteById(id)
    }

    override fun allPartnersByCategoryId(categoryId: Long): List<BusinessPartnerEntity> {
        return businessPartnerRepository.findAllByCategoryId(categoryId)
    }

    override fun getPartnerAccount(partnerId: Long): PartnerAccountResponse {
        val partner = businessPartnerRepository.findByIdOrNull(partnerId)
            ?: throw BusinessNotFoundException()

        val account = partner.account ?: throw AccountNotFoundException()

        return PartnerAccountResponse(
            id = partner.id!!,
            name = partner.name,
            account = account.toDto(),
            logoUrl = partner.logoUrl!!,
            category = partner.category!!.toDto()
        )
    }
}