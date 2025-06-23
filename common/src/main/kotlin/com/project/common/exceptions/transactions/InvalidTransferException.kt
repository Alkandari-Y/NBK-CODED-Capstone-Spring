package com.project.common.exceptions.transactions

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class InvalidTransferException(
    override val message: String = "Invalid transfer.",
    override val code: ErrorCode = ErrorCode.INVALID_TRANSFER,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)