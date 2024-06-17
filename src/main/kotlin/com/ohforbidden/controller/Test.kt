package com.ohforbidden.controller

import com.ohforbidden.global.auth.JwtClaims
import com.ohforbidden.global.auth.JwtProvider
import com.ohforbidden.global.auth.LoginResponse
import com.ohforbidden.global.auth.Role
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class Test(private val jwtProvider: JwtProvider) {
    @GetMapping("/auth/test/tokens")
    fun getTokens(): LoginResponse {
        val jwtClaims = JwtClaims(1L, "test@example.com", Role.USER)
        val accessToken = jwtProvider.createAccessToken(jwtClaims)
        val refreshToken = jwtProvider.createRefreshToken(jwtClaims)
        return LoginResponse(accessToken, refreshToken)
    }

    @GetMapping("/test/token/auth")
    fun testTokenAuth(): String {
        return "Token authentication success!!"
    }

    @GetMapping("/admin/token/auth")
    fun adminTokenAuth(): String {
        return "Admin Token authentication success!!"
    }
}