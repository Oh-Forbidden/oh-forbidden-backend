package com.ohforbidden.controller

import com.ohforbidden.global.auth.JwtClaims
import com.ohforbidden.global.auth.JwtProvider
import com.ohforbidden.global.auth.LoginResponse
import com.ohforbidden.global.auth.Role
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/test")
class Test(private val jwtProvider: JwtProvider) {
    @GetMapping("/tokens")
    fun getTokens(): LoginResponse {
        val jwtClaims = JwtClaims(1, "test@example.com", Role.USER)
        val accessToken = jwtProvider.createAccessToken(jwtClaims)
        val refreshToken = jwtProvider.createRefreshToken(jwtClaims)
        return LoginResponse(accessToken, refreshToken)
    }
}