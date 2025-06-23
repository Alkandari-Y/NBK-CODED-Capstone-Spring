package com.project.banking.repositories

import com.project.banking.entities.UserXpEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserXpRepository: JpaRepository<UserXpEntity, Long> {
    fun findByUserId(userId: Long): UserXpEntity?
}