package com.project.banking.repositories

import com.project.banking.entities.BusinessPartnerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BusinessPartnerRepository: JpaRepository<BusinessPartnerEntity, Long> {
    @Query("""
    SELECT bp FROM BusinessPartnerEntity bp
    WHERE bp.category.name = :categoryName
""")
    fun findByCategoryName(@Param("categoryName") categoryName: String): List<BusinessPartnerEntity>

    @Query("""
    SELECT bp FROM BusinessPartnerEntity bp
    WHERE bp.category.id = :categoryId
""")
    fun findByCategoryId(@Param("categoryId") categoryId: Long): List<BusinessPartnerEntity>
}