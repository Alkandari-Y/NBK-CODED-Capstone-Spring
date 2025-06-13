package com.project.common.exceptions.auth

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class InvalidCredentialsException(
    override val message: String = "Invalid credentials",
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val code: ErrorCode = ErrorCode.INVALID_CREDENTIALS,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)