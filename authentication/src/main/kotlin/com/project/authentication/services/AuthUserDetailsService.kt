package com.project.authentication.services

import com.project.authentication.entities.AuthUserDetails
import com.project.authentication.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService (
    private val usersRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")

        return AuthUserDetails(
            userId = user.id!!,
            username = user.username,
            password = user.password,
            authorities = user.roles.toSet()
                .map { SimpleGrantedAuthority(it.name) }
        )
    }
}