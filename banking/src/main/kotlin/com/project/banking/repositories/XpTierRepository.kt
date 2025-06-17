package com.project.banking.repositories

import com.project.banking.entities.XpTierEntity
import org.springframework.data.jpa.repository.JpaRepository

interface XpTierRepository: JpaRepository<XpTierEntity, Long> {}