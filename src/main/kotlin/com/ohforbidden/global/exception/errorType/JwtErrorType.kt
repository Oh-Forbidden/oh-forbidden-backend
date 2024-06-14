package com.ohforbidden.global.exception.errorType

import com.ohforbidden.global.exception.ErrorType
import org.springframework.http.HttpStatus

/**
 * [JWT 에러 타입]
 * Error Code: 1100 ~ 1199
 */
enum class JwtErrorType : ErrorType {
    NO_TOKEN {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val errorCode = "1101"
        override val message = "토큰이 필요합니다."
    },

    // SignatureException, MalformedJwtException, JwtException
    INVALID_TOKEN {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val errorCode = "1102"
        override val message = "유효하지 않은 토큰입니다."
    },

    // ExpiredJwtException
    EXPIRED_TOKEN {
        override val httpStatus = HttpStatus.UNAUTHORIZED
        override val errorCode = "1103"
        override val message = "토큰이 만료되었습니다."
    }
}