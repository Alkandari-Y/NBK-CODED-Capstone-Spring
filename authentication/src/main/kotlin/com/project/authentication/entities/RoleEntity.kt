package com.project.authentication.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "roles")
data class RoleEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
    val name: String = "",

) : GrantedAuthority {
    override fun getAuthority(): String = name
}

