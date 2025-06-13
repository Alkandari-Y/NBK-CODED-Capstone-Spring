package com.project.common.exceptions.auth

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus


data class InvalidTokenException(
    override val message: String = "Token validation failed or returned no response.",
    override val code: ErrorCode = ErrorCode.UNAUTHORIZED,
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)