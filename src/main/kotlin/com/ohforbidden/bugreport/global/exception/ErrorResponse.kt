package com.ohforbidden.bugreport.global.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val uuid: String,
    val httpStatus: Int,
    val errorCode: String,
    val message: String?,
    val timeStamp: LocalDateTime = LocalDateTime.now()
)