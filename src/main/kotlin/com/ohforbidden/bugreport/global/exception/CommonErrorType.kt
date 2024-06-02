package com.ohforbidden.bugreport.global.exception

import org.springframework.http.HttpStatus

/**
 * [공용 에러 타입]
 * Error Code: 0000 ~ 0999
 * - 두번째 자리 에러코드 지정 규칙
 *   - 0: 잘못된 값 때문에 검증에 실패
 *   - 1: 인증/인가 때문에 오류
 *   - 3: conflict 예외
 *   - 9: 외부 API 또는 내부 서버 에러 처리를 내보낼 때
 */
enum class CommonErrorType : ErrorType {
    /**
     * ************* 400 Bad Request *************
     */
    HTTP_MESSAGE_UNREADABLE {
        override val httpStatus = HttpStatus.BAD_REQUEST
        override val errorCode = "0001"
        override val message = "요청 정보를 읽을 수 없습니다."
    },

    INVALID_ARGUMENT {
        override val httpStatus = HttpStatus.BAD_REQUEST
        override val errorCode = "0002"
        override val message = "Request Body 값이 조건에 부합하지 않습니다."
    },

    CONSTRAINT_VIOLATION {
        override val httpStatus = HttpStatus.BAD_REQUEST
        override val errorCode = "0003"
        override val message = "Request Path Variable (또는 Query String) 값이 유효하지 않습니다."
    },

    /**
     * ************* 401 Unauthorized *************
     */
    NO_AUTHORIZATION {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val errorCode = "0103"
        override val message = "해당 요청에 대한 권한이 존재하지 않습니다."
    },

    /**
     * ************* 408 REQUEST_TIMEOUT *************
     */
    TIME_OUT {
        override val httpStatus = HttpStatus.REQUEST_TIMEOUT
        override val errorCode = "0902"
        override val message = "External API의 요청시간이 초과하였습니다."
    },

    /**
     * ************* 409 Conflict *************
     */
    OPTIMISTIC_LOCK_FAILURE {
        override val httpStatus = HttpStatus.CONFLICT
        override val errorCode = "0304"
        override val message = "선행 작업으로 인해 요청을 처리할 수 없습니다."
    },

    /**
     * ************* 502 Bad Gateway *************
     */
    REST_CLIENT_BAD_RESPONSE {
        override val httpStatus = HttpStatus.BAD_GATEWAY
        override val errorCode = "0901"
        override val message = "External API의 응답 형태가 유효하지 않습니다."
    },

    /**
     * ************* 500 Internal Server Error *************
     */
    UNKNOWN_EXCEPTION {
        override val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        override val errorCode = "0999"
        override val message = "내부 서버 에러가 발생했습니다."
    }
}