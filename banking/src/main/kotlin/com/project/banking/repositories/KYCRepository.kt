package com.project.banking.repositories

import com.project.banking.entities.KYCEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KYCRepository: JpaRepository<KYCEntity, Long> {
    fun findByUserId(userId: Long): KYCEntity?
}