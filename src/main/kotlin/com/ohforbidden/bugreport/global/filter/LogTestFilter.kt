package com.ohforbidden.bugreport.global.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import java.util.UUID

class LogTestFilter() : Filter {
    private val log = KotlinLogging.logger{}
    override fun init(filterConfig: FilterConfig) {
        log.info { "@@@ Log Filter Initialized @@@" }
    }
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val requestUri = httpRequest.requestURI
        val uuid = UUID.randomUUID().toString()

        try {
            log.info { "REQUEST $uuid, $requestUri"}
            chain.doFilter(request, response)
        } catch (e: Exception) {
            throw e;
        } finally {
            log.info("RESPONSE $uuid, $requestUri")
        }
    }

    override fun destroy() {
        log.info { "@@@ Log Filter destroyed @@@" }
    }
}