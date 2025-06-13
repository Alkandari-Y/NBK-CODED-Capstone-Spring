package com.project.banking.repositories

import com.project.banking.entities.AccountEntity
import com.project.common.data.responses.accounts.AccountDto
import com.project.common.enums.AccountType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<AccountEntity, Long> {

    @Query("""
        SELECT NEW com.project.common.data.responses.accounts.AccountDto(
        a.id,
        a.accountNumber,
        a.name,
        a.balance,
        a.active,
        a.ownerId,
        a.accountType
        )
        FROM AccountEntity a
        WHERE a.ownerId = :ownerId AND a.active = true
    """)
    fun findByOwnerIdActive(@Param("ownerId") ownerId: Long): List<AccountDto>


    @Query("""
        SELECT a
        FROM AccountEntity a
        WHERE a.ownerId = :ownerId
    """)
    fun findAllByOwnerId(@Param("ownerId") ownerId: Long): List<AccountEntity>

    @Query("""
        SELECT COUNT(a) FROM AccountEntity a 
        WHERE a.ownerId = :userId 
        AND a.active = true 
        AND a.accountType = :accountType
    """)
    fun getAccountCountByUserId(
        @Param("userId") userId: Long,
        @Param("accountType") accountType: AccountType
    ): Long


    fun findByAccountNumber(accountNumber: String): AccountEntity?
}