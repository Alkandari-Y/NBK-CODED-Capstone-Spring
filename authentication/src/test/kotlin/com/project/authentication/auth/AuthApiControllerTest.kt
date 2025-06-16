package com.project.authentication.auth

import com.ninjasquad.springmockk.MockkBean
import com.project.common.data.requests.authentication.LoginRequest
import com.project.common.data.requests.authentication.RegisterCreateRequest
import com.project.authentication.entities.AuthUserDetails
import com.project.authentication.services.JwtService
import com.project.authentication.services.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.security.Principal
import java.util.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.project.authentication.config.NoCacheTestConfig
import com.project.authentication.entities.RoleEntity
import com.project.authentication.entities.UserEntity
import com.project.authentication.repositories.RoleRepository
import com.project.authentication.repositories.UserRepository
import com.project.common.exceptions.auth.UserExistsException
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration(
    excludeName = [
        "com.hazelcast.spring.cache.HazelcastCacheConfiguration",
        "com.hazelcast.config.Config",
        "com.hazelcast.client.config.ClientConfig",
        "com.hazelcast.spring.HazelcastInstanceFactoryBean"
    ]
)
@Import(NoCacheTestConfig::class)
class AuthApiControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @MockkBean
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @MockkBean
    private lateinit var jwtService: JwtService

    private lateinit var testUser: UserEntity
    private lateinit var testRoles: List<RoleEntity>
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    @BeforeEach
    fun setupSecurityContext() {
        val auth = TestingAuthenticationToken(testUser.username, null)
        SecurityContextHolder.getContext().authentication = auth
    }

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
        roleRepository.deleteAll()

        val roleUser = roleRepository.findByName("ROLE_USER") ?: roleRepository.save(RoleEntity(name = "ROLE_USER"))

        val encodedPassword = passwordEncoder.encode("passworD123")
        testUser = UserEntity(
            username = "testuser",
            password = encodedPassword,
            email = "test@example.com",
            isActive = true,
            roles = mutableSetOf(roleUser)
        )
        userRepository.save(testUser)

        listOf("ROLE_ADMIN", "ROLE_DEVELOPER").forEach {
            if (!roleRepository.existsByName(it)) {
                roleRepository.save(RoleEntity(name = it))
            }
        }

        testRoles = testUser.roles.toList()
        accessToken = "test.access.token"
        refreshToken = "test.refresh.token"
    }

    @Test
    fun `register should create a new user and return JWT tokens`() {
        val registerRequest = RegisterCreateRequest(
            username = "testuser",
            password = "passworD123",
            email = "test@example.com"
        )

        every { userService.createUser(any()) } returns testUser
        every { jwtService.generateTokenPair(testUser, any()) } returns Pair(accessToken, refreshToken)

        mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.access").value(accessToken))
            .andExpect(jsonPath("$.refresh").value(refreshToken))


        verify { userService.createUser(registerRequest) }
        verify {
            jwtService.generateTokenPair(
                testUser,
                match { it.contains("ROLE_USER") }
            )
        }
    }

    @Test
    fun `register should return 400 when user already exists`() {
        val registerRequest = RegisterCreateRequest(
            username = "testuser",
            password = "passworD123",
            email = "test@example.com"
        )

        every { userService.createUser(any()) } throws UserExistsException("User already exists")


        mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `login should authenticate user and return JWT tokens`() {
        val loginRequest = LoginRequest(
            username = "testuser",
            password = "passworD123"
        )

        val authToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        val authentication = mockk<Authentication>()
        val userDetails = AuthUserDetails(
            userId = testUser.id!!,
            username = testUser.username,
            password = testUser.password,
            authorities = testRoles
        )

        every { authenticationManager.authenticate(any()) } returns authentication
        every { authentication.principal } returns userDetails
        every { userService.findUserByUsername(testUser.username) } returns testUser
        every { jwtService.generateTokenPair(testUser, any()) } returns Pair(accessToken, refreshToken)

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.access").value(accessToken))
            .andExpect(jsonPath("$.refresh").value(refreshToken))

        verify { userService.findUserByUsername(testUser.username) }
        verify { jwtService.generateTokenPair(testUser, testUser.roles.map { it.name }) }
    }

    @Test
    fun `validate should return 403 when token is missing`() {
        mockMvc.perform(post("/api/v1/auth/validate"))
            .andExpect(status().isForbidden)
    }


    @Test
    fun `login should return 401 when authentication fails`() {
        val loginRequest = LoginRequest(
            username = "testuser",
            password = "wrongpassword"
        )

        every { authenticationManager.authenticate(any()) } throws UsernameNotFoundException("User not found")

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `validate should return user details when token is valid`() {
        val token = "test.access.token"

        every { jwtService.extractUsername(token) } returns testUser.username
        every { jwtService.isTokenValid(token, any()) } returns true
        every { jwtService.extractRoles(token) } returns testUser.roles.map { it.name }
        every { userService.findUserByUsername(testUser.username) } returns testUser

        mockMvc.perform(
            post("/api/v1/auth/validate")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value(testUser.id.toString()))
            .andExpect(jsonPath("$.isActive").value(testUser.isActive))
            .andExpect(jsonPath("$.username").value(testUser.username))
            .andExpect(jsonPath("$.email").value(testUser.email))

        verify { userService.findUserByUsername(testUser.username) }
    }



    @Test
    fun `validate should return 401 when user is not found`() {
        val principal = mockk<Principal>()

        every { principal.name } returns "nonexistent"
        every { userService.findUserByUsername("nonexistent") } returns null

        mockMvc.perform(
            post("/api/v1/auth/validate")
                .principal(principal)
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `login should throw UsernameNotFoundException when principal is not AuthUserDetails`() {
        val loginRequest = LoginRequest(
            username = "ghostuser",
            password = "passworD123"
        )

        val authentication = mockk<Authentication>()
        every { authenticationManager.authenticate(any()) } returns authentication
        every { authentication.principal } returns "invalid_principal"

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `login should throw UsernameNotFoundException when user not found after authentication`() {
        val loginRequest = LoginRequest(
            username = "ghostuser",
            password = "passworD123"
        )

        val authentication = mockk<Authentication>()
        val userDetails = AuthUserDetails(
            userId = 999L,
            username = "ghostuser",
            password = "passworD123",
            authorities = listOf()
        )

        every { authenticationManager.authenticate(any()) } returns authentication
        every { authentication.principal } returns userDetails
        every { userService.findUserByUsername("ghostuser") } returns null

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    fun `validate should return 500 when user id is null`() {
        val token = "test.access.token"
        val userWithoutId = testUser.copy(id = null)

        every { jwtService.extractUsername(token) } returns testUser.username
        every { jwtService.isTokenValid(token, any()) } returns true
        every { jwtService.extractRoles(token) } returns testUser.roles.map { it.name }
        every { userService.findUserByUsername(testUser.username) } returns userWithoutId

        mockMvc.perform(
            post("/api/v1/auth/validate")
                .header("Authorization", "Bearer $token")
        ).andExpect(status().isInternalServerError)
    }

    @Test
    fun `register should return 400 for invalid input`() {
        val invalidRequest = RegisterCreateRequest(
            username = "",
            password = "123",
            email = "invalid-email",
            civilId = ""
        )

        mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        ).andExpect(status().isBadRequest)
    }

}
