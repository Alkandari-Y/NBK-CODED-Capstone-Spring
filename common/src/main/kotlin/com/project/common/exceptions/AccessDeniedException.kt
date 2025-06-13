package com.project.common.exceptions

import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

class AccessDeniedException(
    override val message: String = "You do not have permission to perform this action.",
    override val code: ErrorCode = ErrorCode.ACCESS_DENIED,
    override val httpStatus: HttpStatus = HttpStatus.FORBIDDEN,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
