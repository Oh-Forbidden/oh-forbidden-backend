package com.ohforbidden.global.auth

data class JwtClaims(
    val userId: String,
    val authType: AuthType
)