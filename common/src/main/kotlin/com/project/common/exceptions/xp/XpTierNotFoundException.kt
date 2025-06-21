package com.project.common.exceptions.xp

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class XpTierNotFoundException(
    override val message: String = "XP tier not found",
    override val code: ErrorCode = ErrorCode.XP_TIER_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
