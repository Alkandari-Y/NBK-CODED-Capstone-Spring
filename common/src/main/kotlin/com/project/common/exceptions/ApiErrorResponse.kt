package com.project.common.exceptions

data class ValidationError(
    val field: String,
    val message: String
)

data class ApiErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val code: String,
    val path: String,
    val fieldErrors: List<ValidationError>? = null,
    val traceId: String? = null
)
