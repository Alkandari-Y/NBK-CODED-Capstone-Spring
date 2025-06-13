package com.project.banking.mappers

import com.project.banking.entities.KYCEntity
import com.project.common.data.requests.kyc.KYCRequest
import com.project.common.data.responses.kyc.KYCResponse
import com.project.common.utils.dateFormatter
import java.time.LocalDate

fun KYCRequest.toEntity(userId: Long) = KYCEntity(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = LocalDate.parse(dateOfBirth, dateFormatter),
    salary = salary,
    nationality = nationality
)

fun KYCEntity.toResponse() = KYCResponse(
    id = id!!,
    userId = userId!!,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = dateOfBirth,
    salary = salary,
    nationality = nationality
)