package com.ohforbidden.global.auth

import com.ohforbidden.domain.user.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * UserDetails 생성 로직 포함
 */
@Service
class CustomUserDetailsService(private val encoder: BCryptPasswordEncoder) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        // 임시 유저 생성. 추후에 DB에서 User 읽어오기
        val user = User(
            id = 1,
            email = "user",
            password = encoder.encode("1234"),
            nickname = "loginTestUser",
            role = Role.USER
        ) // ?: AuthException(AuthErrorType.NOT_REGISTERED_EMAIL)
        val authorities = user.role.auth.split(",").map { SimpleGrantedAuthority(it) }
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            authorities
        )
    }
}