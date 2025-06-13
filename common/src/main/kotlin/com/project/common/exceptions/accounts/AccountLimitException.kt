package com.project.common.exceptions.accounts

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class AccountLimitException(
    override val message: String = "Account limit reached. Please visit your nearest NBK branch.",
    override val code: ErrorCode = ErrorCode.ACCOUNT_LIMIT_REACHED,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
