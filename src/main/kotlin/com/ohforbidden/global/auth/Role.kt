package com.ohforbidden.global.auth

enum class Role(val auth: String) {
    USER("ROLE_USER"),
    ADMIN("ROLE_USER,ROLE_ADMIN")
}