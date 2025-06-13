package com.project.common.exceptions.kycs

import com.project.common.exceptions.APIException
import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class AccountNotVerifiedException(
    override val message: String = "KYC not verified.",
    override val code: ErrorCode = ErrorCode.ACCOUNT_NOT_VERIFIED,
    override val httpStatus: HttpStatus = HttpStatus.FORBIDDEN,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
