package com.project.common.exceptions.xp

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class XpBelowMinException(
    override val message: String = "XP must be greater than 0",
    override val code: ErrorCode = ErrorCode.INVALID_INPUT,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
