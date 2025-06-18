package com.project.banking.repositories

import com.project.banking.entities.BusinessPartnerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessPartnerRepository: JpaRepository<BusinessPartnerEntity, Long> {
}