package com.project.authentication.filters

import com.project.authentication.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = extractToken(request)
        if (token != null && jwtService.isTokenValid(token, jwtService.extractUsername(token))) {
            val username = jwtService.extractUsername(token)
            val userDetails = userDetailsService.loadUserByUsername(username)
            val authorities = jwtService.extractRoles(token)
                .map { SimpleGrantedAuthority(it) }

            val authToken = UsernamePasswordAuthenticationToken(
                userDetails, null, authorities
            )
            SecurityContextHolder.getContext().authentication = authToken

        }
        chain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}