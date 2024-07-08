package com.ohforbidden.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class ServletCachingFilter() : OncePerRequestFilter() { // 모든 서블릿 요청에 일관된 필터 적용을 위해 OncePerRequestFilter 사용

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        chain.doFilter(wrappedRequest, wrappedResponse)
        wrappedResponse.copyBodyToResponse() // 해당 부분을 통해 response 다시 읽을 수 있도록 설정
    }
}