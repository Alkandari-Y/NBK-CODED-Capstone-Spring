package com.project.recommendation.config

import com.project.recommendation.filters.RemoteAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
                    "/api/v1/store-locations/**", "/api/v1/promotions/**",
                ).permitAll()
                    .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/category-scores/**",
                        "/api/v1/recommendations/account-score"
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