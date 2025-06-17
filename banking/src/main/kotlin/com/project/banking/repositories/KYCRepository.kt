package com.project.banking.repositories

import com.project.banking.entities.KycEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KYCRepository: JpaRepository<KycEntity, Long> {
    fun findByUserId(userId: Long): KycEntity?
}