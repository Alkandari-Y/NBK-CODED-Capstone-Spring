package com.project.authentication.config

import com.project.authentication.entities.RoleEntity
import com.project.authentication.entities.UserEntity
import com.project.authentication.repositories.RoleRepository
import com.project.authentication.repositories.UserRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class DataSeeder(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val userRoleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun seedInitialData() {
        val roles = seedRoles()
        seedUsers(roles)
    }


    private fun seedUsers(roles: List<RoleEntity>) {
        if (userRepository.count() == 0L) {
            val defaultPassword = passwordEncoder.encode("passworD123")

            userRepository.saveAll(
    listOf(
                    UserEntity(
                        username = "admin",
                        password = defaultPassword,
                        email = "admin@mail.com",
                        roles = roles.toMutableSet()
                    ),
                    UserEntity(
                        username = "testUser",
                        password = defaultPassword,
                        email = "testUser@mail.com",
                        roles = roles.filter { it.name == "ROLE_USER" }.toMutableSet()
                    )
                )
            )
            println("Seeded default users.")

        } else {
            println("Users already exist. Skipping seeding.")
        }
    }

    private fun seedRoles(): List<RoleEntity>{
        if (roleRepository.count() == 0L) {
            val roles = roleRepository.saveAll(
                listOf("ROLE_ADMIN", "ROLE_USER")
                    .map { RoleEntity(name = it) }
            )
            println("Seeded default roles.")
            return roles
        } else {
            println("Roles already exist. Skipping seeding.")
            return roleRepository.findAll()
        }
    }
}
