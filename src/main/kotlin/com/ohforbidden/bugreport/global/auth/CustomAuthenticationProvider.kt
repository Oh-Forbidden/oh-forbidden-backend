package com.ohforbidden.bugreport.global.auth

import com.ohforbidden.bugreport.global.exception.AuthException
import com.ohforbidden.bugreport.global.exception.errorType.AuthErrorType
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

/**
 * 로그인 로직 포함
 */
@Component
class CustomAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val encoder: BCryptPasswordEncoder
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val email = authentication.name
        val password = authentication.credentials.toString()

        // 유저 조회
        val userDetails = userDetailsService.loadUserByUsername(email)

        // 비밀번호 검증
        if (!encoder.matches(password, userDetails.password)) {
            throw AuthException(AuthErrorType.INVALID_PASSWORD)
        }

        val authorities = userDetails.authorities.map { SimpleGrantedAuthority(it.authority) }

        return UsernamePasswordAuthenticationToken(userDetails, password, authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}