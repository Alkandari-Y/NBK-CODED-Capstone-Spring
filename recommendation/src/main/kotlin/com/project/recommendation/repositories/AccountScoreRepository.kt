package com.project.recommendation.repositories

import com.project.recommendation.entities.AccountScoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AccountScoreRepository : JpaRepository<AccountScoreEntity, Long> {

    @Query(
        """
            SELECT ase 
                FROM AccountScoreEntity ase 
                WHERE ase.userId = :userId 
                AND ase.accountId = :accountId
        """
    )
    fun findAllByUserIdAndAccountId(@Param("userId") userId: Long,
                           @Param("accountId") accountId: Long
    ): AccountScoreEntity?
}
