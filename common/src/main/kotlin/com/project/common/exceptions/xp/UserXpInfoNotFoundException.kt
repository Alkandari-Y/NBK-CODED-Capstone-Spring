package com.project.common.exceptions.xp

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus


data class UserXpInfoNotFoundException(
    override val message: String = "User XP info not found. Did you complete the KYC registration process?",
    override val code: ErrorCode = ErrorCode.XP_INFO_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
