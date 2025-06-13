package com.project.common.exceptions.accounts

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class InvalidTransferException(
    override val message: String = "Cannot transfer to the same account.",
    override val code: ErrorCode = ErrorCode.INVALID_TRANSFER,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)