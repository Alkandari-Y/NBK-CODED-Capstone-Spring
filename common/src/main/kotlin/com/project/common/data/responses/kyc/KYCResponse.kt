package com.project.common.data.responses.kyc

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDate

data class KYCResponse(
    val id: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var dateOfBirth: LocalDate? = null,
    val salary: BigDecimal,
    val nationality: String
)


