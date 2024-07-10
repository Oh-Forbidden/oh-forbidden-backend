package com.ohforbidden.bugreport.global.auth

enum class TokenType(val subject: String) {
    ACCESS("access_token"),
    REFRESH("refresh_token")
}