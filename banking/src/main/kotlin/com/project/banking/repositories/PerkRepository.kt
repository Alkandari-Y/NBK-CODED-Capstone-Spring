package com.project.banking.repositories

import com.project.banking.entities.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PerRepository : JpaRepository<AccountEntity, Long> {}
