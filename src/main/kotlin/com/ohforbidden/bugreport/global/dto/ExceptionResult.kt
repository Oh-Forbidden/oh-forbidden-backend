package com.ohforbidden.bugreport.global.dto

import java.time.LocalDateTime

data class ExceptionResult(
    val uuid: String,
    val httpStatus: Int,
    val errorCode: String,
    val message: String?,
    val timeStamp: LocalDateTime = LocalDateTime.now()
)