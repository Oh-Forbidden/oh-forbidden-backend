package com.ohforbidden.bugreport.global.config

import com.ohforbidden.bugreport.global.filter.MDCLoggingFilter
import jakarta.servlet.Filter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig() : WebMvcConfigurer {
//    override fun addInterceptors(registry: InterceptorRegistry) {
//        registry
//            .addInterceptor(LogInterceptor())
//            .order(1)
//            .addPathPatterns("/**")
//            .excludePathPatterns("/css/**", "/*.ico", "/error")
//    }

    @Bean
    fun mdcLoggingFilter(): FilterRegistrationBean<Filter> {
        val filterRegistrationBean = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = MDCLoggingFilter()
        filterRegistrationBean.order = 1
        filterRegistrationBean.addUrlPatterns("/*")
        return filterRegistrationBean
    }
}