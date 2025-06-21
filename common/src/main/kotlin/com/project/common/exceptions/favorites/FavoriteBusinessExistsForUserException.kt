package com.project.common.exceptions.favorites

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class FavoriteBusinessExistsForUserException(
    override val message: String = "Business Partner already exists in favorites",
    override val code: ErrorCode = ErrorCode.BUSINESS_PARTNER_FAVORITE_EXISTS,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)