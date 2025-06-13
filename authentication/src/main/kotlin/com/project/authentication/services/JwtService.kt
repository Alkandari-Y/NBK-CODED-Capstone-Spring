package com.project.authentication.services

import com.project.authentication.entities.UserEntity

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import kotlin.String


@Component
class JwtService (
    @Value("\${jwt.secret}") private val secretKeyString: String,
){
    private val key: SecretKey = Keys.hmacShaKeyFor(secretKeyString.encodeToByteArray())

//    private val accessTokenExpirationMs: Long = 1000 * 60 * 15 // Reset later after demo
    private val accessTokenExpirationMs: Long = 1000 * 60 * 60 * 24 * 7 // for Demo
    private val refreshTokenExpirationMs: Long = 1000 * 60 * 60 * 24 * 7

    fun generateTokenPair(user: UserEntity, authorities: List<String>): Pair<String, String> =
        Pair(
            generateAccessToken(user, authorities),
            generateRefreshToken(user, authorities)
        )

    fun generateAccessToken(user: UserEntity, authorities: List<String>): String =
        generateToken(user, key, accessTokenExpirationMs, "access", authorities)

    fun generateRefreshToken(user: UserEntity, authorities: List<String>): String =
        generateToken(user, key, refreshTokenExpirationMs, "refresh", authorities)

    fun isTokenType(token: String, expectedType: String): Boolean {
        return try {
            val jwt = parseToken(token)
            val type = jwt["type"] as? String
            type == expectedType
        } catch (e: Exception) {
            false
        }
    }


    private fun generateToken(
        user: UserEntity,
        key: SecretKey,
        expirationMs: Long,
        type: String,
        authorities: List<String>
    ): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)

        return Jwts.builder()
            .setSubject(user.username)
            .claim("userId", user.id)
            .claim("roles", authorities)
            .claim("isActive", user.isActive)
            .claim("type", type)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key)
            .compact()
    }


    fun extractUsername(token: String): String =
        parseToken(token).subject


    fun extractRoles(token: String): List<String> =
        parseToken(token).get("roles", ArrayList::class.java).map { it.toString() }

    fun isTokenValid(token: String, username: String): Boolean =
        extractUsername(token) == username

    private fun parseToken(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            
}