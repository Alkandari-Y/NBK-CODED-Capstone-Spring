package com.project.authentication.auth

import com.project.common.data.requests.authentication.LoginRequest
import com.project.common.data.requests.authentication.RefreshRequest
import com.project.common.data.requests.authentication.RegisterCreateRequest
import com.project.authentication.entities.AuthUserDetails
import com.project.authentication.services.JwtService
import com.project.authentication.services.UserService
import com.project.common.data.responses.authentication.JwtResponse
import com.project.common.data.responses.authentication.ValidateTokenResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@RestController
@RequestMapping("/api/v1/auth")
class AuthApiController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationProvider,
    private val jwtService: JwtService,
) {
    @PostMapping(path = ["/register"])
    fun register(
        @Valid @RequestBody userCreateRequest: RegisterCreateRequest
    ): ResponseEntity<JwtResponse> {
        val userEntity = userService.createUser(
            userCreateRequest
        )
        val (access, refresh) = jwtService.generateTokenPair(userEntity, userEntity.roles.map { it.name })
        return ResponseEntity(JwtResponse(access, refresh), HttpStatus.OK)
    }

    @PostMapping(path = ["/login"])
    fun login(
        @Valid @RequestBody loginRequestDto: LoginRequest
    ): ResponseEntity<JwtResponse> {
        val authToken = UsernamePasswordAuthenticationToken(
            loginRequestDto.username,
            loginRequestDto.password
        )

        val authentication = authenticationManager.authenticate(authToken)
        val userDetails = authentication.principal as? AuthUserDetails
            ?: throw UsernameNotFoundException("User not found")
        val user = userService.findUserByUsername(userDetails.username)
            ?: throw UsernameNotFoundException("User not found")

        val (access, refresh) = jwtService.generateTokenPair(user, user.roles.map { it.name })
        return ResponseEntity(JwtResponse(access, refresh), HttpStatus.OK)
    }


    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshRequest: RefreshRequest): ResponseEntity<JwtResponse> {
        val refreshToken = refreshRequest.refresh
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        return try {
            val username = jwtService.extractUsername(refreshToken)
            if (!jwtService.isTokenType(refreshToken, "refresh")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }

            val user = userService.findUserByUsername(username)
                ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

            val (access, newRefresh) = jwtService.generateTokenPair(user, user.roles.map { it.name })
            ResponseEntity.ok(JwtResponse(access, newRefresh))
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }


    @PostMapping("/validate")
    fun checkToken(
        principal: Principal
    ): ValidateTokenResponse {

        val user = userService.findUserByUsername(principal.name)
            ?: throw UsernameNotFoundException("User not found")

        return ValidateTokenResponse(
            userId = user.id ?: throw IllegalStateException("User id is null"),
            isActive = user.isActive,
            roles = user.roles.map { it.name },
            username = user.username,
            email = user.email
        )
    }
}