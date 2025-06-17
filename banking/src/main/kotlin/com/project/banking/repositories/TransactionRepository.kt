package com.project.banking.repositories

import com.project.banking.entities.TransactionEntity
import com.project.banking.transactions.dtos.TransactionDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository: JpaRepository<TransactionEntity, Long> {

    @Query(
        value = """
        SELECT NEW com.project.banking.transactions.dtos.TransactionDetails(
            sa.accountNumber,
            da.accountNumber,
            te.amount,
            te.createdAt,
            c.name
        )
        FROM TransactionEntity te
            JOIN te.sourceAccount sa
            JOIN te.destinationAccount da
            JOIN te.category c
        WHERE sa.accountNumber = :accountNumber
            OR da.accountNumber= :accountNumber  
     """)
    fun findRelatedTransactions(
        @Param("accountNumber") accountNumber: String,
    ): List<TransactionDetails>


    @Query("""
        SELECT new com.project.banking.transactions.dtos.TransactionDetails(
            t.sourceAccount.accountNumber,
            t.destinationAccount.accountNumber,
            t.amount,
            t.createdAt,
            c.name
        )
        FROM TransactionEntity t
        JOIN t.sourceAccount sa
        JOIN t.destinationAccount da
        JOIN t.category c
        WHERE sa.ownerId = :userId OR da.ownerId = :userId
    """)
    fun findAllByUserId(@Param("userId") userId: Long): List<TransactionDetails>
}