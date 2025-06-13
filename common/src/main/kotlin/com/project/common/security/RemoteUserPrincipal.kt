package com.project.common.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class RemoteUserPrincipal(
    private val userId: Long,
    private val username: String,
    private val email: String,
    private val isActive: Boolean,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String? = null // You can leave this null if it's not needed

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    fun getUserId(): Long = userId
    fun getEmail(): String = email
}
