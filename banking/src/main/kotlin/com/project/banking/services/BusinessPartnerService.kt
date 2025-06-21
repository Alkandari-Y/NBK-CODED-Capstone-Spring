package com.project.banking.services

import com.project.banking.entities.BusinessPartnerEntity
import com.project.common.data.requests.businessPartners.CreateBusinessPartnerRequest
import com.project.common.data.responses.authentication.UserInfoDto

interface BusinessPartnerService {

    fun getAllPartners(): List<BusinessPartnerEntity>
    fun getPartnerById(id: Long): BusinessPartnerEntity?
    fun createBusinessPartner(
        businessPartnerRequest: CreateBusinessPartnerRequest,
        userInfoDto: UserInfoDto
    ): BusinessPartnerEntity
    fun deletePartnerById(id: Long)
    fun allPartnersByCategoryId(categoryId: Long): List<BusinessPartnerEntity>
}