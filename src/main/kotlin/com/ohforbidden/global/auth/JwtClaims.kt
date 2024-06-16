package com.ohforbidden.global.auth

data class JwtClaims(
    val userId: Long,
    val email: String,
    val role: Role
)
