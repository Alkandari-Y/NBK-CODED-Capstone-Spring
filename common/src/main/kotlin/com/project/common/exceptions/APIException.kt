package com.project.common.exceptions

import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

open class APIException(
    override val message: String = "Internal Server Error",
    open val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    open val code: ErrorCode = ErrorCode.INTERNAL_SERVER_ERROR,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)