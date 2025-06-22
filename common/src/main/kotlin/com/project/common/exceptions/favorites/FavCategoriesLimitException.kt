package com.project.common.exceptions.favorites

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class FavCategoriesLimitException(
    override val message: String = "Max limit reached",
    override val code: ErrorCode = ErrorCode.FAV_CATEGORIES_MAX_LIMIT,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
