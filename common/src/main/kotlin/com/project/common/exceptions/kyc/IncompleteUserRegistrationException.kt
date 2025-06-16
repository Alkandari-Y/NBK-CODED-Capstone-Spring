package com.project.common.exceptions.kyc

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus


data class IncompleteUserRegistrationException(
    override val message: String = "Please complete the KYC registration process.",
    override val code: ErrorCode = ErrorCode.INCOMPLETE_USER_REGISTRATION,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
