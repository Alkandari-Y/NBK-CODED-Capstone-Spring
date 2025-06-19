package com.project.banking.services

import com.project.banking.entities.BusinessPartnerEntity
import com.project.banking.repositories.BusinessPartnerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BusinessServiceImpl(
    private val businessPartnerRepository: BusinessPartnerRepository,
): BusinessPartnerService {
    override fun getAllPartners(): List<BusinessPartnerEntity> {
        return businessPartnerRepository.findAll()
    }

    override fun getPartnerById(id: Long): BusinessPartnerEntity? {
        return businessPartnerRepository.findByIdOrNull(id)
    }

    override fun deletePartnerById(id: Long) {
        businessPartnerRepository.deleteById(id)
    }

    override fun allPartnersByCategoryId(categoryId: Long): List<BusinessPartnerEntity> {
        return businessPartnerRepository.findByCategoryId(categoryId)
    }

}