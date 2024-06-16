package com.ohforbidden.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class EncoderConfig {

    @Bean
    fun encoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}