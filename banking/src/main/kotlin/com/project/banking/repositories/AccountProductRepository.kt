package com.project.banking.repositories

import com.project.banking.entities.AccountProductEntity
import com.project.common.enums.AccountType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountProductRepository : JpaRepository<AccountProductEntity, Long> {
    fun findByAccountType(accountType: AccountType): List<AccountProductEntity>

    fun findByName(name: String): AccountProductEntity?

    fun findByAccountTypeAndName(accountType: AccountType, name: String): AccountProductEntity?

    fun findFirstByAccountTypeAndName(accountType: AccountType, name: String): AccountProductEntity?

}