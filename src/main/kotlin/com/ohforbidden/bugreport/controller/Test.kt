package com.ohforbidden.bugreport.controller

import com.ohforbidden.bugreport.domain.user.UserRepository
import com.ohforbidden.bugreport.domain.user.entity.User
import com.ohforbidden.bugreport.global.auth.JwtClaims
import com.ohforbidden.bugreport.global.auth.JwtProvider
import com.ohforbidden.bugreport.global.auth.LoginResponse
import com.ohforbidden.bugreport.global.auth.Role
import com.ohforbidden.bugreport.infrastructure.message.slack.SlackTransmitter
import com.ohforbidden.bugreport.infrastructure.message.slack.dto.SignUpParam
import com.slack.api.webhook.WebhookResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class Test(
    private val jwtProvider: JwtProvider,
    private val slackTransmitter: SlackTransmitter,
    private val userRepository: UserRepository
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

    @PostMapping("/user")
    fun adminTokenAuth(
        @RequestBody body: CreateUserTestReq
    ): User? {
        val result = try {
            val dto = body.toEntity()
            userRepository.save(dto)
        } catch (e: DataIntegrityViolationException) {
            println("$$$$$$$$$$$$$$$$$$$$$$ Duplicate Key 발생 $$$$$$$$$$$$$$$$$$$$$$$")
            throw e
        }
        return userRepository.findById(result.id)
    }
}

data class CreateUserTestReq(
    val nickname: String,
    val email: String,
    val password: String,
    val role: Role,
    val imageUrl: String
) {
    fun toEntity(): User {
        return User(nickname, email, password, role, imageUrl)
    }
}