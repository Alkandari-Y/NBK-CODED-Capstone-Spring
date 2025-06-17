package com.project.common.exceptions.accountProducts

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class AccountProductNotFoundException(
    override val message: String = "Account Product not found",
    override val code: ErrorCode = ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)