package com.project.notification.filters

import com.project.common.data.responses.authentication.toUserInfoDto
import com.project.common.security.RemoteUserPrincipal
import com.project.notification.providers.JwtAuthProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class RemoteAuthenticationFilter(
    private val jwtAuthProvider: JwtAuthProvider
) : OncePerRequestFilter() {

    val publicUrls = listOf(
        "/api/v1/notifications/search",
    )

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val isPublicUrl = request.method == "GET" && publicUrls.any { request.requestURI.contains(it) }
        val isPublicPost = request.method == "POST" && (request.requestURI.startsWith("/api/v1/notifications"))

        return isPublicUrl || isPublicPost
    }


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val bearerToken: String? = request.getHeader("Authorization")
        if (bearerToken.isNullOrBlank() || !bearerToken.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization header is missing or invalid")
            return
        }

        try {
            val token = bearerToken.substring(7).trim()
            val result = jwtAuthProvider.authenticateToken(token)
            val authorities = result.roles.map { SimpleGrantedAuthority( it ) }

            val userRemoteUserPrincipal = RemoteUserPrincipal(
                username = result.username,
                userId = result.userId,
                isActive = result.isActive,
                email = result.email,
                authorities = authorities.toSet()
            )

            request.setAttribute("authUser", result.toUserInfoDto())

            val authToken = UsernamePasswordAuthenticationToken(
                userRemoteUserPrincipal,
                null,
                authorities
            )
            SecurityContextHolder.getContext().authentication = authToken

            filterChain.doFilter(request, response)

        } catch (ex: Exception) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Invalid token")
        }
    }
}