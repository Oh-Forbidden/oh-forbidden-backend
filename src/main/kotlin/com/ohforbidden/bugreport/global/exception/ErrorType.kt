package com.ohforbidden.bugreport.global.exception

import org.springframework.http.HttpStatus

interface ErrorType {
    val httpStatus: HttpStatus
    val errorCode: String
    val message: String
}