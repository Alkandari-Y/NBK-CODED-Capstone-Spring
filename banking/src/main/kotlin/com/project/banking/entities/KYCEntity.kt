package com.project.banking.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "kycs")
data class KYCEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false, unique = true)
    val userId: Long?,

    @Column(name="first_name")
    val firstName: String,

    @Column(name="last_name")
    val lastName: String,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: LocalDate,

    @Column(name="nationality", nullable = false)
    val nationality: String,

    @Column(name = "salary", precision = 9, scale = 2, nullable = false)
    val salary: BigDecimal,
) {
    constructor(): this(
        id =null,
        userId=null,
        firstName="",
        lastName="",
        dateOfBirth=LocalDate.now(),
        nationality="",
        salary=BigDecimal(0.0),
    )
}

