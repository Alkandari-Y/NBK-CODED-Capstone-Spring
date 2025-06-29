package com.project.common.exceptions.notifications

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class NotificationNotFoundException(
    override val message: String = "Notification not found",
    override val code: ErrorCode = ErrorCode.NOTIFICATION_NOT_FOUND,
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
    override val cause: Throwable? = null
) : APIException(message, httpStatus, code, cause)