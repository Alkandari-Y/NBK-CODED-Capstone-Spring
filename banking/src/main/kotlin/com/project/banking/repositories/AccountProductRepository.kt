package com.project.banking.repositories

import com.project.banking.entities.AccountProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountProductRepository: JpaRepository<AccountProductEntity, Long> {
}