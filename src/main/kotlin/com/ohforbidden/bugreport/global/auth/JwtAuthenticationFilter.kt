package com.ohforbidden.bugreport.global.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT 검증 필터: Access token 인증 로직
 */
@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        if (request.servletPath.startsWith("/auth")) {
            chain.doFilter(request, response)
            return
        }

        val jwtToken = jwtProvider.resolveAccessToken(request)
        val payload = jwtProvider.getPayload(jwtToken, TokenType.ACCESS)
        val userDetails = customUserDetailsService.loadUserByUsername(payload.email)
        val authentication =
            UsernamePasswordAuthenticationToken(userDetails, userDetails.password, userDetails.authorities)

        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication

        chain.doFilter(request, response)
    }
}