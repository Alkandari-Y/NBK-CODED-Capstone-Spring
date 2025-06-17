package com.project.authentication.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    val id: Long? = null,

    @Column(name="username", nullable = false, unique = true)
    val username: String = "",

    @Column(name="email", nullable = false, unique = true)
    val email: String = "",

    @Column(name="password")
    val password: String = "",

    @Column(name="is_active", nullable = false)
    val isActive: Boolean = false,

    @CreationTimestamp
    @Column(name="created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<RoleEntity> = mutableSetOf(),
) {
    constructor() : this(
        id = null,
        username = "",
        email = "",
        password = "",
        isActive = false,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        roles = mutableSetOf(),
    )
}
