package com.project.banking.repositories

import com.project.banking.entities.XpTierEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface XpTierRepository: JpaRepository<XpTierEntity, Long> {}