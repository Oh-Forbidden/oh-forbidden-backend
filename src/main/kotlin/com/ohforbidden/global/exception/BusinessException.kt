package com.ohforbidden.global.exception

class BusinessException(
    val errorType: ErrorType,
    override val cause: Throwable? = null,
) : RuntimeException(errorType.message, cause)