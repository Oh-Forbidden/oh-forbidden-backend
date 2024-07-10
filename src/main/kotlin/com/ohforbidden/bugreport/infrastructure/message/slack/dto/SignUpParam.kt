package com.ohforbidden.bugreport.infrastructure.message.slack.dto

data class SignUpParam(
    val username: String,
    val email: String,
    val code: String
)
