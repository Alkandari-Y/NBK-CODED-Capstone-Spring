package com.project.common.exceptions.auth

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class UserExistsException (
    override val message: String = "User with these credentials already Exists",
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val code: ErrorCode = ErrorCode.USER_ALREADY_EXISTS,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)