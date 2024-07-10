package com.ohforbidden.bugreport.global.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import java.util.UUID

@Order(Ordered.HIGHEST_PRECEDENCE)
class MDCLoggingFilter() : Filter {
    private val log = KotlinLogging.logger{}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val logId = UUID.randomUUID().toString()
        MDC.put("LOG_ID", logId)
        chain.doFilter(request, response)
        MDC.clear()
    }
}