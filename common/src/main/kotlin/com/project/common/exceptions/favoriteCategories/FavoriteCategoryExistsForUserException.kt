package com.project.common.exceptions.favoriteCategories

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class FavoriteCategoryExistsForUserException(
    override val message: String = "Category already exists in favorites",
    override val code: ErrorCode = ErrorCode.CATEGORY_FAVORITE_EXISTS,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)

