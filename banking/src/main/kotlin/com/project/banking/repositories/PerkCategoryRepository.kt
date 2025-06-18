package com.project.banking.repositories

import com.project.banking.entities.PerkCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface AccountPerkRepository: JpaRepository<PerkCategoryEntity, Long> {
}