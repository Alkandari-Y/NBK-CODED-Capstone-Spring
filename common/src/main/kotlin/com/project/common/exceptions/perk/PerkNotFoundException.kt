package com.project.common.exceptions.perk

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class PerkNotFoundException (
    override val message: String = "Perk not found",
    override val code: ErrorCode = ErrorCode.PERK_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)
