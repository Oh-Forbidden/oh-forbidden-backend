package com.ohforbidden.bugreport.global.auth

import com.ohforbidden.global.exception.AuthException
import com.ohforbidden.global.exception.errorType.AuthErrorType

enum class Role(val auth: String) {
    USER("ROLE_USER"),
    ADMIN("ROLE_USER,ROLE_ADMIN")
}

fun getRoleByAuth(auth: String): Role {
    return Role.entries.find { it.auth == auth }
        ?: throw AuthException(AuthErrorType.NOT_FOUND_ROLE_BY_AUTH)
}