package com.ohforbidden.global.dto

import com.ohforbidden.global.util.createUtcDateTime
import java.time.ZonedDateTime

data class ErrorResponse(
    val uuid: String,
    val httpStatus: Int,
    val errorCode: String,
    val message: String?,
    val timeStamp: ZonedDateTime = createUtcDateTime()
)