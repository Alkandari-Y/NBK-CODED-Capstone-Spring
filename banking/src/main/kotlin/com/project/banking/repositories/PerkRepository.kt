package com.project.banking.repositories

import com.project.banking.entities.PerkEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PerkRepository : JpaRepository<PerkEntity, Long> {
    fun findAllByAccountProductId(id: Long): List<PerkEntity>?
    fun findAllByCategoryId(id: Long): List<PerkEntity>
}
