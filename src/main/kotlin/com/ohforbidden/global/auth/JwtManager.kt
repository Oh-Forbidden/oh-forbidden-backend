package com.ohforbidden.global.auth

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.Date

@Component
class JwtManager(
    @Value("\${jwt.access.private}")
    private val accessPrivateRsa: String,

    @Value("\${jwt.access.public}")
    private val accessPublicRsa: String,

    @Value("\${jwt.access.expiration-time}")
    private val accessExpirationTime: String,

    @Value("\${jwt.refresh.private}")
    private val refreshPrivateRsa: String,

    @Value("\${jwt.refresh.public}")
    private val refreshPublicRsa: String,

    @Value("\${jwt.refresh.expiration-time}")
    private val refreshExpirationTime: String
) {
    private val keyFactory = KeyFactory.getInstance("RSA")
    private val accessPrivateKey = createPrivateKey(accessPrivateRsa)
    private val refreshPrivateKey = createPrivateKey(refreshPrivateRsa)

    // TODO: 리프레시 토큰 생성 함수
    fun createAccessToken(claims: JwtClaims): String {
        // TODO: 리프레시 토큰 검증 로직
        val expirationDate = createJwtExpirationDate(accessExpirationTime.toLong())
        return createJwt(TokenType.ACCESS, claims, expirationDate)
    }

    private fun createJwt(type: TokenType, claims: JwtClaims, expirationDate: Date): String {
        val privateKey = when(type) {
            TokenType.ACCESS -> accessPrivateKey
            TokenType.REFRESH -> refreshPrivateKey
        }

        return Jwts.builder()
            .subject(TokenType.ACCESS.name)
            .header().add("typ", "JWT").and()
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .claim("userId", claims.userId) // 회원 아이디
            .claim("authType", claims.authType)
            .signWith(privateKey, Jwts.SIG.RS512) // 암호화가 더 중요하기 때문에 privateKey로 암호화
            .compact()
    }

    private fun createPublicKey(rsa: String): PublicKey {
        val rsaByteArray = parseRsaToByteArray(rsa)
        val keySpecX509 = X509EncodedKeySpec(rsaByteArray)
        return keyFactory.generatePublic(keySpecX509)
    }

    private fun createPrivateKey(rsa: String): PrivateKey {
        val rsaByteArray = parseRsaToByteArray(rsa)
        val keySpecPKCS8 = PKCS8EncodedKeySpec(rsaByteArray)
        return keyFactory.generatePrivate(keySpecPKCS8)
    }

    private fun parseRsaToByteArray(rsa: String) = Base64.getDecoder().decode(rsa)
    private fun createJwtExpirationDate(period: Long) = Date(System.currentTimeMillis() + period)
}