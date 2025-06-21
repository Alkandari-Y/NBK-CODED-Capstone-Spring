package com.project.common.exceptions.xp

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class XpTierNameTakenException(
    override val message: String = "XP tier name taken",
    override val code: ErrorCode = ErrorCode.PERK_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
