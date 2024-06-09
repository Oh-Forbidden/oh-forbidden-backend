package com.ohforbidden.bugreport.global.dto

import com.ohforbidden.bugreport.global.util.createUtcDateTime
import java.time.ZonedDateTime

data class ErrorResponse(
    val uuid: String,
    val httpStatus: Int,
    val errorCode: String,
    val message: String?,
    val timeStamp: ZonedDateTime = createUtcDateTime()
)