package com.project.banking.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate


@Entity
@Table(name = "kycs")
data class KycEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
     var id: Long? = null,

    @Column(name = "user_id", nullable = false)
     var userId: Long? = null,

    @Column(name = "first_name", nullable = false)
     var firstName: String? = null,

    @Column(name = "last_name", nullable = false)
     var lastName: String? = null,

    @Column(name = "date_of_birth", nullable = false)
     var dateOfBirth: LocalDate? = null,

    @Column(name = "nationality", nullable = false)
     var nationality: String? = null,

    @Column(name = "salary", nullable = false, precision = 9, scale = 3)
     var salary: BigDecimal? = null,

    @Column(name = "civil_id", nullable = false)
     var civilId: String? = null,

    @Column(name = "mobile_number", nullable = false)
     var mobileNumber: String? = null,

    @Column(name = "is_verified", nullable = false)
     var isVerified: Boolean = false,
    )