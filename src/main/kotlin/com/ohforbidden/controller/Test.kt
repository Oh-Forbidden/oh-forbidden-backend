package com.ohforbidden.controller

import com.ohforbidden.global.auth.JwtClaims
import com.ohforbidden.global.auth.JwtProvider
import com.ohforbidden.global.auth.LoginResponse
import com.ohforbidden.global.auth.Role
import com.ohforbidden.infrastructure.message.slack.SlackTransmitter
import com.ohforbidden.infrastructure.message.slack.dto.SignUpParam
import com.slack.api.webhook.WebhookResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class Test(
    private val jwtProvider: JwtProvider,
    private val slackTransmitter: SlackTransmitter
) {
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

    @PostMapping("/auth/sign-up")
    fun adminTokenAuth(
        @RequestBody body: SignUpParam
    ): WebhookResponse? {
        return slackTransmitter.sendSignUpApplication(body)
    }
}