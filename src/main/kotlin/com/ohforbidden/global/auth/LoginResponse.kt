package com.ohforbidden.global.auth

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)
