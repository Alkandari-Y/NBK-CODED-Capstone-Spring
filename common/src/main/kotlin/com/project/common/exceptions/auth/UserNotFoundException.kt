package com.project.common.exceptions.auth

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class UserNotFoundException(
    override val message: String = "User Not Found",
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val code: ErrorCode = ErrorCode.USER_NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)