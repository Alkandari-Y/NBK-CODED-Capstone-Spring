package com.project.authentication.config

import com.project.common.exceptions.APIException
import com.project.common.exceptions.ApiErrorResponse
import com.project.common.enums.ErrorCode
import com.project.common.exceptions.ValidationError
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import io.jsonwebtoken.security.SignatureException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import java.time.Instant

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredToken(
        ex: ExpiredJwtException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiErrorResponse(
                timestamp = Instant.now().toString(),
                status = HttpStatus.UNAUTHORIZED.value(),
                error = HttpStatus.UNAUTHORIZED.reasonPhrase,
                message = "Expired JWT token. Please re-login",
                code = ErrorCode.EXPIRED_TOKEN.name,
                path = request.getDescription(false).removePrefix("uri=")
            )
        )
    }

    @ExceptionHandler(APIException::class)
    fun handleAPIBaseException(
        ex: APIException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(ex.httpStatus).body(
            ApiErrorResponse(
                timestamp = Instant.now().toString(),
                status = ex.httpStatus.value(),
                error = ex.httpStatus.reasonPhrase,
                message = ex.message,
                code = ex.code.name,
                path = request.getDescription(false).removePrefix("uri=")
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiErrorResponse(
                timestamp = Instant.now().toString(),
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                message = ex.message ?: "Unexpected error occurred",
                code = ErrorCode.INTERNAL_SERVER_ERROR.name,
                path = request.getDescription(false).removePrefix("uri=")
            )
        )
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(
        ex: UsernameNotFoundException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiErrorResponse(
                timestamp = Instant.now().toString(),
                status = HttpStatus.UNAUTHORIZED.value(),
                error = HttpStatus.UNAUTHORIZED.reasonPhrase,
                message = "Invalid Credentials",
                code = ErrorCode.INVALID_CREDENTIALS.name,
                path = request.getDescription(false).removePrefix("uri=")
            )
        )
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(
        ex: BadCredentialsException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiErrorResponse(
                timestamp = Instant.now().toString(),
                status = HttpStatus.UNAUTHORIZED.value(),
                error = HttpStatus.UNAUTHORIZED.reasonPhrase,
                message = "Invalid Credentials",
                code = ErrorCode.INVALID_CREDENTIALS.name,
                path = request.getDescription(false).removePrefix("uri=")
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        val fieldErrors = ex.bindingResult.fieldErrors.map {
            ValidationError(it.field, it.defaultMessage ?: "Invalid value")
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiErrorResponse(
                timestamp = Instant.now().toString(),
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.reasonPhrase,
                message = "Validation failed",
                code = ErrorCode.INVALID_INPUT.name,
                path = request.getDescription(false).removePrefix("uri="),
                fieldErrors = fieldErrors
            )
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidJson(
        ex: HttpMessageNotReadableException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Bad Request",
                message = "Malformed JSON",
                code = "MALFORMED_REQUEST",
                path = (request as? ServletWebRequest)?.request?.requestURI!!,
                timestamp = Instant.now().toString()
            )
        )
    }

    @ExceptionHandler(value = [AccessDeniedException::class, AuthorizationDeniedException::class])
    fun handleAccessDeniedVariants(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ApiErrorResponse(
                status = HttpStatus.FORBIDDEN.value(),
                error = "Forbidden",
                message = ex.message ?: "Access denied",
                code = "FORBIDDEN",
                path = (request as? ServletWebRequest)?.request?.requestURI!!,
                timestamp = Instant.now().toString()
            )
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Bad Request",
                message = ex.message ?: "Invalid input",
                code = "INVALID_ARGUMENT",
                path = (request as? ServletWebRequest)?.request?.requestURI!!,
                timestamp = Instant.now().toString()
            )
        )
    }

    @ExceptionHandler(SignatureException::class)
    fun handleInvalidJwtSignature(
        ex: SignatureException,
        request: WebRequest
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity(
            ApiErrorResponse(
                status = HttpStatus.UNAUTHORIZED.value(),
                error = HttpStatus.UNAUTHORIZED.reasonPhrase,
                message = "Invalid JWT signature. Please log in again.",
                code = ErrorCode.INVALID_TOKEN.name,
                path = (request as? ServletWebRequest)?.request?.requestURI ?: "unknown",
                timestamp = Instant.now().toString(),
            ),
            HttpStatus.UNAUTHORIZED
        )
    }

}
