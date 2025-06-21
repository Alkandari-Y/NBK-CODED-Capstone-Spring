package com.project.banking.config

import com.project.banking.filters.RemoteAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val remoteAuthenticationFilter: RemoteAuthenticationFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    HttpMethod.GET,
                    "/api/v1/partners/**", "/api/v1/categories"
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(remoteAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build();
    }
}