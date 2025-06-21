package com.project.common.exceptions.categories

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class CategoryNotFoundException(
    override val message: String = "Category not found",
    override val code: ErrorCode = ErrorCode.CATEGORY_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)