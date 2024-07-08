package com.ohforbidden.bugreport.global.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.UUID

@Component
class LogInterceptor() : HandlerInterceptor {
    private val log = KotlinLogging.logger {}

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        val uuid = UUID.randomUUID().toString()
        MDC.put("LOG_ID", uuid)

        if (handler is HandlerMethod) {
            val handlerMethod = handler
        }
        log.info("REQUEST [$uuid] [$requestURI] [$handler]")

        return true
    }

    // 예외 발생 시 호출되지 않음
    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        val logId = MDC.get("LOG_ID")
        val requestURI = request.requestURI
        log.info("AFTER HANDLER [${logId}] [$requestURI] [$handler]")
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val logId = MDC.get("LOG_ID")
        val requestURI = request.requestURI
        log.info("RESPONSE [$logId] [$requestURI] [$handler]")

        if (ex != null) {
            log.error("afterCompletion error!!", ex)
        }
    }
}