package com.project.common.exceptions.accounts

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus


data class AccountNotActiveException(
    val accountNumber: String,
    override val cause: Throwable? = null
) : APIException(
    message = "Account $accountNumber is not active.",
    httpStatus = HttpStatus.BAD_REQUEST,
    code = ErrorCode.ACCOUNT_NOT_ACTIVE,
    cause = cause
)