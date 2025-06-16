package com.project.authentication.services

import io.jsonwebtoken.JwtException
import com.project.authentication.entities.UserEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JwtServiceTest {

    private lateinit var jwtService: JwtService
    private val secret = "supersecretkeythatshouldbe32byteslong123"

    private val testUser = UserEntity(
        id = 1L,
        username = "testuser",
        password = "password",
        email = "test@example.com",
        isActive = true,
        roles = mutableSetOf()
    )

    private val roles = listOf("ROLE_USER", "ROLE_ADMIN")

    @BeforeEach
    fun setup() {
        jwtService = JwtService(secret)
    }

    @Test
    fun `generateTokenPair should return access and refresh tokens`() {
        val (access, refresh) = jwtService.generateTokenPair(testUser, roles)

        assertTrue(access.isNotBlank())
        assertTrue(refresh.isNotBlank())
    }

    @Test
    fun `extractUsername should return correct username`() {
        val accessToken = jwtService.generateAccessToken(testUser, roles)
        val username = jwtService.extractUsername(accessToken)

        assertEquals(testUser.username, username)
    }

    @Test
    fun `extractRoles should return all roles`() {
        val accessToken = jwtService.generateAccessToken(testUser, roles)
        val extractedRoles = jwtService.extractRoles(accessToken)

        assertEquals(roles, extractedRoles)
    }

    @Test
    fun `isTokenValid should return true for valid username`() {
        val accessToken = jwtService.generateAccessToken(testUser, roles)
        val isValid = jwtService.isTokenValid(accessToken, testUser.username)

        assertTrue(isValid)
    }

    @Test
    fun `parseToken should throw for tampered token`() {
        val token = jwtService.generateAccessToken(testUser, roles)
        val tampered = token + "xyz"

        assertThrows<JwtException> {
            jwtService.extractUsername(tampered)
        }
    }
}
