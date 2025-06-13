package com.project.common.exceptions.kycs

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class KycNotFoundException(
    val userId: Long,
    override val cause: Throwable? = null
) : APIException(
    message = "KYC record for user ID $userId not found.",
    httpStatus = HttpStatus.NOT_FOUND,
    code = ErrorCode.KYC_NOT_FOUND,
    cause = cause
)