package com.ohforbidden.bugreport.global

import cccv.global.auth.AuthContext
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.RestClientException
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.ContentCachingRequestWrapper
import org.yaml.snakeyaml.util.UriEncoder
import java.net.SocketTimeoutException
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionHandler(
    private val authContextThreadLocal: ThreadLocal<AuthContext>,
    private val uuidLoggingThreadLocal: ThreadLocal<String>
) {
    private val log = KotlinLogging.logger {}

    // 프론트에 공개하고 싶은 예외가 발생했으나, 로그에 남길 필요가 없는 경우(예: 중복 요청에 대한 예외처리)
    @ExceptionHandler
    fun handleResponseException(e: ResponseException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity.status(e.errorType.httpStatus).body(
            ErrorResponseDto(
                e.errorType.httpStatus.value(),
                e.errorType.errorCode,
                e.errorType.message,
                LocalDateTime.now(),
                uuidLoggingThreadLocal.get()
            )
        )
    }

    // 비지니스 로직 예외가 발생한 경우와 의존하는 다른 API서버의 예외지만 프론트에 공개되어야 하는 경우(예: 입력값이 잘못 되었을때)
    @ExceptionHandler
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponseDto> {
        val errorResponseDto = logError(
            e,
            "======= BusinessException 로깅 시작(ErrorCode: ${e.errorType.errorCode}) =======",
            e.errorType
        )

        return ResponseEntity.status(e.errorType.httpStatus).body(errorResponseDto)
    }

    private fun logError(e: Exception, headerMessage: String, errorType: ErrorType): ErrorResponseDto {
        val time = LocalDateTime.now()

        val requestAttributes = RequestContextHolder.getRequestAttributes()
        val request = (requestAttributes as ServletRequestAttributes).request as ContentCachingRequestWrapper
        val params = request.parameterMap.entries.joinToString(",") {
            "{${it.key} = ${it.value.joinToString(",")}}"
        }

        log.error(e) {
            """

            $headerMessage

            ====== Request 정보 ======
            -Time = $time
            -UUID = ${MDC.get("LOG_ID") ?: ""}
            -RequestURI = ${UriEncoder.decode(request.requestURI)}
            -RequestMethod = ${request.method}
            -RequestParam = $params
            -RequestBody = ${String(request.contentAsByteArray)}
            -Token = ${authContextThreadLocal.get() ?: ""}
            -RequestHeader = ${request.headerNames.toList().joinToString(",") { "$it = ${request.getHeader(it)}" }}
            ▼▼▼▼▼▼▼▼▼▼▼▼▼▼STACK TRACE▼▼▼▼▼▼▼▼▼▼▼▼▼▼
            """.trimIndent()
        }

        return ErrorResponseDto(
            errorType.httpStatus.value(),
            errorType.errorCode,
            errorType.message,
            time,
            uuidLoggingThreadLocal.get()
        )
    }

    // 타임아웃 에러 처리
    @ExceptionHandler
    fun handleTimeOutException(e: SocketTimeoutException): ResponseEntity<ErrorResponseDto> {
        return handleBusinessException(BusinessException(CommonErrorType.TIME_OUT, e))
    }

    // RestClient 에러 처리
    @ExceptionHandler
    fun handleRestClientException(e: RestClientException): ResponseEntity<ErrorResponseDto> {
        return handleBusinessException(BusinessException(CommonErrorType.REST_CLIENT_BAD_RESPONSE, e))
    }

    // DTO Valid 위반 에러 처리
    @ExceptionHandler
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> {
        return handleBusinessException(BusinessException(CommonErrorType.INVALID_ARGUMENT_EXCEPTION, e))
    }

    @ExceptionHandler
    fun httpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponseDto> {
        return handleBusinessException(BusinessException(CommonErrorType.HTTP_MESSAGE_UNREADABLE, e))
    }

    @ExceptionHandler
    fun optimisticLockFailureException(e: OptimisticLockingFailureException): ResponseEntity<ErrorResponseDto> {
        return handleBusinessException(BusinessException(CommonErrorType.OPTIMISTIC_LOCK_FAILURE, e))
    }

    // 기타 모든 에러 처리
    @ExceptionHandler()
    fun handleException(e: Exception): ResponseEntity<ErrorResponseDto> {
        return handleBusinessException(BusinessException(CommonErrorType.UNKNOWN_EXCEPTION, e))
    }
}
