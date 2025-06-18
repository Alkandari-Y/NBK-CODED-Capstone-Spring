package com.project.banking.repositories

import com.project.banking.entities.XpHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface XpHistoryRepository: JpaRepository<XpHistoryEntity, Long> {}