package com.project.authentication.repositories

import com.project.authentication.entities.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long> {
    fun findByName(name: String): RoleEntity?
    fun findAllByNameIn(names: Collection<String>): List<RoleEntity>

    fun existsByName(name: String): Boolean
}
