package com.ohforbidden.global.exception.errorType

import com.ohforbidden.global.exception.ErrorType
import org.springframework.http.HttpStatus

/**
 * [auth 에러 타입]
 * Error Code: 1000 ~ 1999
 * - 두번째 자리 에러코드 지정 규칙
 *   - 0: JWT Error
 *   - 1: Login Error
 */
enum class AuthErrorType : ErrorType {
    /**
     * ************* JWT Error *************
     */

    // IllegalIdentifierException
    NULL_OR_EMPTY_TOKEN_STRING {
        override val httpStatus = HttpStatus.BAD_REQUEST
        override val errorCode = "1001"
        override val message = "토큰 String이 Null 또는 Empty 입니다."
    },

    NO_TOKEN {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val errorCode = "1002"
        override val message = "토큰이 필요합니다."
    },

    // SignatureException, MalformedJwtException, JwtException
    INVALID_TOKEN {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val errorCode = "1003"
        override val message = "유효하지 않은 토큰입니다."
    },

    // ExpiredJwtException
    EXPIRED_TOKEN {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val errorCode = "1004"
        override val message = "토큰이 만료되었습니다."
    },

    INVALID_BEARER_TOKEN_FORMAT {
        override val httpStatus = HttpStatus.BAD_REQUEST
        override val errorCode = "1005"
        override val message = "bearer 토큰 형식을 맞춰주세요."
    },

    /**
     * ************* Login Error *************
     */
     NOT_REGISTERED_EMAIL {
        override val httpStatus = HttpStatus.BAD_REQUEST
        override val errorCode = "1101"
        override val message = "가입하지 않은 이메일입니다."
    },

    INVALID_PASSWORD {
        override val httpStatus = HttpStatus.BAD_REQUEST
        override val errorCode = "1102"
        override val message = "비밀번호가 일치하지 않습니다."
    }
}