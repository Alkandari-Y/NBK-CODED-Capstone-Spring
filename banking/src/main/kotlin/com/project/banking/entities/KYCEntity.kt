package com.project.banking.entities

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.math.BigDecimal
import java.time.LocalDate


@Entity
@Table(name = "kycs")
@Data
@NoArgsConstructor
@AllArgsConstructor
class KycEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Long? = null

    @Column(name = "user_id", nullable = false)
    private var userId: Long? = null

    @Column(name = "first_name", nullable = false)
    private var firstName: String? = null

    @Column(name = "last_name", nullable = false)
    private var lastName: String? = null

    @Column(name = "date_of_birth", nullable = false)
    private var dateOfBirth: LocalDate? = null

    @Column(name = "nationality", nullable = false)
    private var nationality: String? = null

    @Column(name = "salary", nullable = false, precision = 8, scale = 2)
    private var salary: BigDecimal? = null

    @Column(name = "civil_id", nullable = false)
    private var civilId: String? = null

    @Column(name = "mobile_number", nullable = false)
    private var mobileNumber: String? = null

    @Column(name = "is_verified", nullable = false)
    private var isVerified = false
}