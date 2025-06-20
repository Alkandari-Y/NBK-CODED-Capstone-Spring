package com.project.common.exceptions.businessPartners

import com.project.common.enums.ErrorCode
import com.project.common.exceptions.APIException
import org.springframework.http.HttpStatus

data class BusinessPartnerNotFoundException (
    override val cause: Throwable? = null
) : APIException(
    message = "Business Partner not found",
    httpStatus = HttpStatus.NOT_FOUND,
    code = ErrorCode.BUSINESS_PARTNER_NOT_ACTIVE,
    cause = cause
)