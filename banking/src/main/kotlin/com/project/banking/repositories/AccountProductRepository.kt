package com.project.banking.repositories

import com.project.banking.entities.AccountProductEntity
import com.project.banking.projections.AccountProductView
import com.project.common.enums.AccountType
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AccountProductRepository : JpaRepository<AccountProductEntity, Long> {
    fun findByAccountType(accountType: AccountType): List<AccountProductEntity>

    fun findByName(name: String): AccountProductEntity?

    fun findByAccountTypeAndName(accountType: AccountType, name: String): AccountProductEntity?

    fun findFirstByAccountTypeAndName(accountType: AccountType, name: String): AccountProductEntity?

    @EntityGraph(attributePaths = ["perks", "perks.categories"])
    fun findWithPerksAndCategoriesById(id: Long): AccountProductEntity?

    @Query("SELECT a FROM AccountProductEntity a WHERE a.id = :id")
    fun findProjectedById(@Param("id") id: Long): AccountProductView?
}