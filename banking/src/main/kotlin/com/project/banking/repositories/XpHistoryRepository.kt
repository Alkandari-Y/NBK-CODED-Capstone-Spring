package com.project.banking.repositories

import com.project.banking.entities.XpHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface XpHistoryRepository: JpaRepository<XpHistoryEntity, Long> {
    @Query("""SELECT h FROM XpHistoryEntity h
    JOIN h.userXp ux
    WHERE ux.userId = :userId
    ORDER BY h.id DESC""")
    fun findAllByUserId(@Param("userId") userId: Long): List<XpHistoryEntity>

    @Query(
        """
    SELECT COUNT(*)
    FROM xp_history x
    JOIN user_xp ux ON x.user_xp_id = ux.id
    JOIN transactions t ON x.transaction_id = t.id
    WHERE x.account_product_id = :accountProductId
      AND ux.user_id = :userId
      AND t.created_at >= :after
    """, nativeQuery = true)
    fun countRecentXpEventsByAccountProduct(
        @Param("accountProductId") accountProductId: Long,
        @Param("userId") userId: Long,
        @Param("after") after: LocalDateTime
    ): Int
}