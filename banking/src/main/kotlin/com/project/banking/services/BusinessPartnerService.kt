package com.project.banking.services

import com.project.banking.entities.BusinessPartnerEntity

interface BusinessPartnerService {

    fun getAllPartners(): List<BusinessPartnerEntity>
    fun getPartnerById(id: Long): BusinessPartnerEntity?
    fun deletePartnerById(id: Long)
    fun allPartnersByCategoryId(categoryId: Long): List<BusinessPartnerEntity>
}