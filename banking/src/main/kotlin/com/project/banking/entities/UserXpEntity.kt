package com.project.banking.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor


@Entity
@Table(name = "user_xp")
@Data
@NoArgsConstructor
@AllArgsConstructor
class UserXp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Long? = null

    @Column(name = "user_id", nullable = false)
    private var userId: Long? = null

    @Column(name = "amount", nullable = false)
    private var amount: Long? = null
}