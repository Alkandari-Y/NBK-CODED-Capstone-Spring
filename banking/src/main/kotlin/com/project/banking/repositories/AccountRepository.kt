    package com.project.banking.repositories

    import com.project.banking.entities.AccountEntity
    import com.project.common.data.responses.accounts.AccountDto
    import com.project.common.enums.AccountOwnerType
    import com.project.common.enums.AccountType
    import org.springframework.data.jpa.repository.JpaRepository
    import org.springframework.data.jpa.repository.Query
    import org.springframework.data.repository.query.Param
    import org.springframework.stereotype.Repository

    @Repository
    interface AccountRepository: JpaRepository<AccountEntity, Long> {

        @Query(
            """
            SELECT NEW com.project.common.data.responses.accounts.AccountDto(
            a.id,
            a.accountNumber,
            a.balance,
            a.ownerId,
            a.ownerType,
            ap.id,
            a.accountType
            )
            FROM AccountEntity a
                JOIN a.accountProduct ap
            WHERE a.ownerId = :ownerId AND a.isActive = true
        """
        )
        fun findByOwnerIdActive(@Param("ownerId") ownerId: Long): List<AccountDto>


        @Query(
            """
            SELECT NEW com.project.common.data.responses.accounts.AccountDto(
            a.id,
            a.accountNumber,
            a.balance,
            a.ownerId,
            a.ownerType,
            ap.id,
            a.accountType
            )
            FROM AccountEntity a
                JOIN a.accountProduct ap
            WHERE a.ownerId = :ownerId AND a.isActive = true
        """
        )
        fun findAllByOwnerId(@Param("ownerId") ownerId: Long): List<AccountDto>

        @Query(
            """
            SELECT COUNT(a) FROM AccountEntity a 
            WHERE a.ownerId = :userId 
            AND a.isActive = true 
            AND a.accountType = :accountType
        """
        )
        fun getAccountCountByUserId(
            @Param("userId") userId: Long,
            @Param("accountType") accountType: AccountType
        ): Long


        fun findByAccountNumber(accountNumber: String): AccountEntity?

        fun findByOwnerIdAndAccountType(ownerId: Long, accountType: AccountType): AccountEntity?

        fun findFirstByOwnerIdAndAccountTypeOrderByIdAsc(ownerId: Long, accountType: AccountType): AccountEntity?

        fun existsByOwnerId(ownerId: Long): Boolean

        @Query("""
            SELECT DISTINCT ap.id 
                FROM AccountEntity a 
                JOIN AccountProductEntity ap ON ap = a.accountProduct
            WHERE a.ownerId = :userId AND a.isActive = true AND a.accountType = :accountType
        """)
        fun getAllUniqueAccountProductIdsByUserId(
            @Param("userId") userId: Long,
            @Param("accountType") accountType: AccountType = AccountType.CREDIT
        ): List<Long>
    }