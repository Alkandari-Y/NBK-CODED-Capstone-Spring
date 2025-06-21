package com.project.common.exceptions.promotions

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class PromotionDateException(
    override val message: String = "End date must not be before start date",
    override val code: ErrorCode = ErrorCode.INVALID_INPUT,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)