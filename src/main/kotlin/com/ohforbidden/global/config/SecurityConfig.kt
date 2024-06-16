package com.ohforbidden.global.config

import com.ohforbidden.global.auth.CustomAuthenticationFilter
import com.ohforbidden.global.auth.CustomAuthenticationProvider
import com.ohforbidden.global.auth.JwtAuthenticationFilter
import com.ohforbidden.global.auth.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customAuthenticationProvider: CustomAuthenticationProvider,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtProvider: JwtProvider,
    private val authenticationConfiguration: AuthenticationConfiguration
//    private val customUserDetailsService: CustomUserDetailsService
) {
    @Bean
    fun filterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        val customAuthenticationFilter = CustomAuthenticationFilter(authenticationManager(), jwtProvider)
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login")

        http.csrf { csrf -> csrf.disable() }    //csrf 비활성화
            .authorizeHttpRequests { auth ->    // 권한 설정 (URL 별 접근 권한)
                auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->     // 세션 관리 (무상태 인증을 위해 세션 비활성)
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)  // JWT 필터 추가
            .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authenticationProvider(customAuthenticationProvider)   // 커스텀 인증 프로바이더 설정

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()

        corsConfiguration.setAllowedOriginPatterns(listOf("*"))
        corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
        corsConfiguration.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")
        corsConfiguration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration) // 모든 경로에 대해서 CORS 설정을 적용

        return source
    }

//    @Bean
//    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
//        return JwtAuthenticationFilter(jwtProvider, customUserDetailsService)
//    }

//    @Bean
//    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
//        return http.getSharedObject(AuthenticationConfiguration::class.java).authenticationManager
//    }
    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}