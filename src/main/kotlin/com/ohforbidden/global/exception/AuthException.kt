package com.ohforbidden.global.exception

class AuthException(
    val errorType: ErrorType,
    override val cause: Throwable? = null,
) : RuntimeException(errorType.message, cause)