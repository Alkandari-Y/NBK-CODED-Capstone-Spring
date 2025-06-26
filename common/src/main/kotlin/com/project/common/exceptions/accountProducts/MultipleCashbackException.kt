package com.project.common.exceptions.accountProducts

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class MultipleCashbackException(
    override val message: String = "Only one cashback account allowed per user.",
    override val code: ErrorCode = ErrorCode.ACCOUNT_LIMIT_REACHED,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
