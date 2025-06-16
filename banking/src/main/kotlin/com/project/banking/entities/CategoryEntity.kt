package com.project.banking.entities

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor


@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Long? = null

    @Column(name = "name", nullable = false, unique = true)
    private var name: String? = null
}