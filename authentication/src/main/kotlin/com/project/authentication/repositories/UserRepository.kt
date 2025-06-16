package com.project.authentication.repositories

import com.project.authentication.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
    fun existsByUsernameOrEmail(username: String, email: String): Boolean
}