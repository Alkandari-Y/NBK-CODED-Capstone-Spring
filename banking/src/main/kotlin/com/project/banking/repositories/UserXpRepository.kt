package com.project.banking.repositories

import com.project.banking.entities.UserXpEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserXpRepository: JpaRepository<UserXpEntity, Long> {
}