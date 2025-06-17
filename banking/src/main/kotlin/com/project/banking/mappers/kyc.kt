package com.project.banking.mappers

import com.project.banking.entities.KycEntity
import com.project.common.data.requests.kyc.KYCRequest
import com.project.common.data.responses.kyc.KYCResponse
import com.project.common.utils.dateFormatter
import java.time.LocalDate

fun KYCRequest.toEntity(userId: Long) = KycEntity(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = LocalDate.parse(dateOfBirth, dateFormatter),
    salary = salary,
    nationality = nationality,
    mobileNumber = mobileNumber
)

fun KycEntity.toResponse() = KYCResponse(
    id = id!!,
    userId = userId!!,
    firstName = firstName!!,
    lastName = lastName!!,
    dateOfBirth = dateOfBirth,
    salary = salary!!,
    nationality = nationality!!,
    civilId = civilId,
    mobileNumber = mobileNumber
)