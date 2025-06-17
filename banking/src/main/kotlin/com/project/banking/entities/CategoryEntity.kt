package com.project.banking.entities

import jakarta.persistence.*


@Entity
@Table(name = "categories")
data class CategoryEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null,

    @Column(name = "name", nullable = false, unique = true)
     var name: String? = null
)