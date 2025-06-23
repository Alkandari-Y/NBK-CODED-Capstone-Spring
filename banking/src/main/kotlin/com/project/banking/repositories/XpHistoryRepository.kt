package com.project.banking.repositories

import com.project.banking.entities.XpHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface XpHistoryRepository: JpaRepository<XpHistoryEntity, Long> {
    @Query("""SELECT h FROM XpHistoryEntity h
    JOIN h.userXp ux
    WHERE ux.userId = :userId
    ORDER BY h.id DESC""")
    fun findAllByUserId(@Param("userId") userId: Long): List<XpHistoryEntity>
}