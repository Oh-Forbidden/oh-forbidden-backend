package com.ohforbidden.bugreport.global.auth

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)
