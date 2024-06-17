package com.ohforbidden.global.auth

import com.ohforbidden.global.exception.AuthException
import com.ohforbidden.global.exception.errorType.AuthErrorType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.http.HttpServletRequest
import org.hibernate.boot.model.naming.IllegalIdentifierException
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
class JwtProvider(
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
    private val accessPublicKey = createPublicKey(accessPublicRsa)
    private val refreshPrivateKey = createPrivateKey(refreshPrivateRsa)
    private val refreshPublicKey = createPublicKey(refreshPublicRsa)

    fun createAccessToken(claims: JwtClaims): String = createJwt(TokenType.ACCESS, claims)

    fun createRefreshToken(claims: JwtClaims): String = createJwt(TokenType.REFRESH, claims)

    fun resolveAccessToken(request: HttpServletRequest): String {
        val authHeader = request.getHeader("Authorization") ?: throw AuthException(AuthErrorType.NO_TOKEN)
        return if (authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else {
            throw AuthException(AuthErrorType.INVALID_BEARER_TOKEN_FORMAT)
        }
    }

    fun getPayload(token: String, type: TokenType): JwtClaims {
        val publicKey = when (type) {
            TokenType.ACCESS -> accessPublicKey
            TokenType.REFRESH -> refreshPublicKey
        }

        val claims = try {
            Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .also { validateExpiration(it) }
        } catch (e: UnsupportedJwtException) {
            throw AuthException(AuthErrorType.INVALID_TOKEN, e)
        } catch (e: JwtException) {
            throw AuthException(AuthErrorType.INVALID_TOKEN, e)
        } catch (e: IllegalIdentifierException) {
            throw AuthException(AuthErrorType.NULL_OR_EMPTY_TOKEN_STRING, e)
        }

        return JwtClaims(
            userId = claims["userId", Integer::class.java].toLong(),
            email = claims.get("email", String::class.java),
            role = Role.valueOf(claims.get("role", String::class.java))
        )
    }

    private fun createJwt(type: TokenType, claims: JwtClaims): String {
        val (privateKey, expirationDate) = when (type) {
            TokenType.ACCESS -> Pair(accessPrivateKey, createJwtExpirationDate(accessExpirationTime.toLong()))
            TokenType.REFRESH -> Pair(refreshPrivateKey, createJwtExpirationDate(refreshExpirationTime.toLong()))
        }

        return Jwts.builder()
            .subject(TokenType.ACCESS.subject)
            .header().add("typ", "JWT").and()
            .issuedAt(Date(System.currentTimeMillis()))
            .issuer("oh-forbidden")
            .expiration(expirationDate)
            .claim("userId", claims.userId)
            .claim("email", claims.email)
            .claim("role", claims.role)
            .signWith(privateKey, Jwts.SIG.RS512) // privateKey로 암호화
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

    private fun validateExpiration(payload: Claims) {
        val isExpired = payload.expiration.before(Date(System.currentTimeMillis()))
        if (isExpired) throw AuthException(AuthErrorType.EXPIRED_TOKEN)
    }

    private fun parseRsaToByteArray(rsa: String) = Base64.getDecoder().decode(rsa)

    private fun createJwtExpirationDate(period: Long) = Date(System.currentTimeMillis() + period)
}