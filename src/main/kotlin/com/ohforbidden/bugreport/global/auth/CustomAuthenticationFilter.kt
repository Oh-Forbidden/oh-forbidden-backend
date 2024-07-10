package com.ohforbidden.bugreport.global.auth

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * 로그인 시 거치는 필터: 로그인 시도/성공 시 실행 함수 (로그인 로직은 CustomAuthenticationProvider에 존재)
 */
class CustomAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtProvider: com.ohforbidden.bugreport.global.auth.JwtProvider
) : UsernamePasswordAuthenticationFilter() {
    init { setFilterProcessesUrl("/auth/login") }

    val objectMapper = ObjectMapper()

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val loginRequest = objectMapper.readValue(request.inputStream, com.ohforbidden.bugreport.global.auth.LoginRequest::class.java)
        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        return authenticationManager.authenticate(authenticationToken)
    }

    // TODO : 인증 성공 시 로직 다시 코딩하기
    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val userDetails = authResult.principal as UserDetails
        val userId = 1L // TODO: 나중에 Repository에서 가져오는 코드 추가
        val email = userDetails.username
        val auth = userDetails.authorities.map { "${it.authority}" }.joinToString(",")
        val role = com.ohforbidden.bugreport.global.auth.getRoleByAuth(auth)
        val claims = com.ohforbidden.bugreport.global.auth.JwtClaims(userId, email, role)
        val accessToken = jwtProvider.createAccessToken(claims)
        val refreshToken = jwtProvider.createRefreshToken(claims) // 리프레시 토큰 로직 추가 가능
        response.addHeader("Authorization", "Bearer $accessToken")
        response.addHeader("Refresh-Token", refreshToken)
        response.contentType = "application/json"
        response.writer.write(ObjectMapper().writeValueAsString(
            com.ohforbidden.bugreport.global.auth.LoginResponse(
                accessToken,
                refreshToken
            )
        ))
    }
}