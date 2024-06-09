package com.ohforbidden.controller

import com.ohforbidden.global.exception.BusinessException
import com.ohforbidden.global.exception.CommonErrorType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test/filter")
class Test {
    @GetMapping
    fun filterTestTrigger() {
        println("### Controller Works!! ###")
//        throw RuntimeException("### Controller Throws!! ###")

        throw BusinessException(CommonErrorType.SERVER_ERROR)
    }
}