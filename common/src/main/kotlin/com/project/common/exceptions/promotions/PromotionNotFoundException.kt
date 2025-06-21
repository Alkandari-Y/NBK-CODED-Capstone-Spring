package com.project.common.exceptions.promotions

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class PromotionNotFoundException(
    override val message: String = "Promotion not found",
    override val code: ErrorCode = ErrorCode.PROMOTION_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)