package com.ohforbidden.bugreport.global.exception

import com.ohforbidden.bugreport.global.exception.errorType.AuthErrorType

class AuthException(
    val errorType: AuthErrorType,
    override val cause: Throwable? = null,
) : RuntimeException(errorType.message, cause)