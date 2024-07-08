package com.ohforbidden.global.exception

import com.ohforbidden.global.exception.errorType.CommonErrorType
import com.ohforbidden.global.util.createUtcDateTime
import mu.KotlinLogging
import org.hibernate.exception.ConstraintViolationException
import org.slf4j.MDC
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.RestClientException
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.ContentCachingRequestWrapper
import org.yaml.snakeyaml.util.UriEncoder
import java.net.SocketTimeoutException

@RestControllerAdvice
class GlobalExceptionHandler(
//    private val authContextThreadLocal: ThreadLocal<AuthContext>
) {
    private val log = KotlinLogging.logger {}

    @ExceptionHandler
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        val errorResponseDto = logError(
            e,
            "============= BusinessException 로깅 시작(ErrorCode: ${e.errorType.errorCode}) =============",
            e.errorType
        )

        return ResponseEntity.status(e.errorType.httpStatus).body(errorResponseDto)
    }

    @ExceptionHandler
    fun handleAuthException(e: AuthException): ResponseEntity<ErrorResponse> {
        val errorResponseDto = logError(
            e,
            "============= AuthException 로깅 시작(ErrorCode: ${e.errorType.errorCode}) =============",
            e.errorType
        )

        return ResponseEntity.status(e.errorType.httpStatus).body(errorResponseDto)
    }

    private fun logError(e: Exception, headerMessage: String, errorType: ErrorType): ErrorResponse {
        val time = createUtcDateTime()

        val requestAttributes = RequestContextHolder.getRequestAttributes()
        val request = (requestAttributes as ServletRequestAttributes).request as ContentCachingRequestWrapper
        val params = request.parameterMap.entries.joinToString(",") {
            "{${it.key} = ${it.value.joinToString(",")}}"
        }

        log.error(e) {
//            -Token = ${authContextThreadLocal.get() ?: ""}
            """
            $headerMessage

            ============= Request 정보 =============
            -Time = $time
            -UUID = ${MDC.get("LOG_ID") ?: ""}
            -RequestURI = ${UriEncoder.decode(request.requestURI)}
            -RequestMethod = ${request.method}
            -RequestParam = $params
            -RequestHeader = ${request.headerNames.toList().joinToString(",") { "$it = ${request.getHeader(it)}" }}
            -RequestBody = ${String(request.contentAsByteArray)}
            ▼▼▼▼▼▼▼▼▼▼▼▼▼▼STACK TRACE▼▼▼▼▼▼▼▼▼▼▼▼▼▼
            """.trimIndent()
        }

        return  ErrorResponse(
            MDC.get("LOG_ID"),
            errorType.httpStatus.value(),
            errorType.errorCode,
            errorType.message,
            createUtcDateTime()
        )
    }

    @ExceptionHandler
    fun httpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        return handleBusinessException(BusinessException(CommonErrorType.HTTP_MESSAGE_UNREADABLE, e))
    }

    // DTO Valid 위반 에러 처리
    @ExceptionHandler
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        return handleBusinessException(BusinessException(CommonErrorType.INVALID_ARGUMENT, e))
    }

    // Path Variable & Query Parameter 위반 에러 처리
    @ExceptionHandler
    fun constraintViolationException(e: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        return handleBusinessException(BusinessException(CommonErrorType.CONSTRAINT_VIOLATION, e))
    }

    @ExceptionHandler
    fun optimisticLockFailureException(e: OptimisticLockingFailureException): ResponseEntity<ErrorResponse> {
        return handleBusinessException(BusinessException(CommonErrorType.OPTIMISTIC_LOCK_FAILURE, e))
    }

    // RestClient 에러 처리
    @ExceptionHandler
    fun handleRestClientException(e: RestClientException): ResponseEntity<ErrorResponse> {
        return handleBusinessException(BusinessException(CommonErrorType.EXTERNAL_API_BAD_RESPONSE, e))
    }

    @ExceptionHandler
    fun handleRestClientTimeout(e: SocketTimeoutException): ResponseEntity<ErrorResponse> {
        return handleBusinessException(BusinessException(CommonErrorType.EXTERNAL_API_TIMEOUT, e))
    }

    // 기타 모든 에러 처리
    @ExceptionHandler
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        return handleBusinessException(BusinessException(CommonErrorType.SERVER_ERROR, e))
    }
}
