package com.project.common.exceptions.accounts

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class AccountVerificationException(
    override val message: String = "Account Validation Exception",
    override val code: ErrorCode = ErrorCode.ACCOUNT_VERIFICATION_EXCEPTION,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)