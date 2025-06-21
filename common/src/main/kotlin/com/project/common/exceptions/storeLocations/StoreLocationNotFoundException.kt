package com.project.common.exceptions.storeLocations

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class StoreLocationNotFoundException(
override val message: String = "Store Location not found",
override val code: ErrorCode = ErrorCode.STORE_LOCATION_NOT_FOUND,
override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)