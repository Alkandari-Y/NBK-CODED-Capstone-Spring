package com.project.common.exceptions.businessPartner

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class BusinessNotFoundException(
    override val message: String = "Business Partner not found",
    override val code: ErrorCode = ErrorCode.BUSINESS_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)