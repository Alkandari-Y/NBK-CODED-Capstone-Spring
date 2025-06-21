package com.project.common.exceptions

import com.project.common.enums.ErrorCode
import org.springframework.http.HttpStatus

data class FavBusinessPartnersLimitException(
    override val message: String = "Max limit reached",
    override val code: ErrorCode = ErrorCode.FAV_BUSINESS_PARTNERS_MAX_LIMIT,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
