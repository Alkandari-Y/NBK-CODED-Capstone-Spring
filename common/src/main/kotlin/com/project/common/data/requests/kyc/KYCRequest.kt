package com.project.common.data.requests.kyc

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal

data class KYCRequest(
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
    @field:NotBlank
    @field:DateTimeFormat(pattern = "dd-MM-yyyy")
    val dateOfBirth: String,
    @field:NotNull
    @field:DecimalMin(value = "0.00", inclusive = true, message = "Minimum salary cannot be below 0")
    val salary: BigDecimal,
    @field:NotBlank
    val nationality: String,
)

