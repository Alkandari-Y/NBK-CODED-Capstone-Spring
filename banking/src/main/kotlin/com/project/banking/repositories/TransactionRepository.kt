package com.project.banking.repositories

import com.project.banking.entities.TransactionEntity
import com.project.common.data.responses.transactions.TransactionDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TransactionRepository: JpaRepository<TransactionEntity, Long> {

    @Query(
        value = """
        SELECT NEW com.project.common.data.responses.transactions.TransactionDetails(
            te.id,
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
    fun findRelatedTransactionsByAccountNumber(
        @Param("accountNumber") accountNumber: String,
    ): List<TransactionDetails>

    @Query("""
        SELECT NEW com.project.common.data.responses.transactions.TransactionDetails(
            te.id,
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
        WHERE sa.ownerId = :userId OR da.ownerId = :userId
    """)
    fun findAllByUserId(@Param("userId") userId: Long): List<TransactionDetails>

    @Query("""
    SELECT COUNT(*)
    FROM transactions t
    JOIN accounts a ON t.source_account_id = a.id
    WHERE a.account_product_id = :accountProductId
      AND a.owner_id = :userId
      AND t.created_at >= :after
      """, nativeQuery = true)
    fun countRecentTransactionsByAccountProduct(
        @Param("accountProductId") accountProductId: Long,
        @Param("userId") userId: Long,
        @Param("after") after: LocalDateTime
    ): Int}