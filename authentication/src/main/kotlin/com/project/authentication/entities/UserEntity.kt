package com.project.authentication.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    val id: Long? = null,

    @Column(name="username", nullable = false, unique = true)
    val username: String = "",

    @Column(name="civil_id", nullable = false, unique = true)
    val civilId: String = "",

    @Column(name="email", nullable = false, unique = true)
    val email: String = "",

    @Column(name="password")
    val password: String = "",

    @Column(name="is_active", nullable = false)
    val isActive: Boolean = false,

    @CreationTimestamp
    @Column(name="created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant = Instant.now(),

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
        civilId = "",
        email = "",
        password = "",
        isActive = false,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
        roles = mutableSetOf(),
    )
}
