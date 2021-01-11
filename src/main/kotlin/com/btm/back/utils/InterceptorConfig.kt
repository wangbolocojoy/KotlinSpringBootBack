package com.btm.back.utils

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class InterceptorConfig : WebMvcConfigurerAdapter() {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**") // 拦截所有请求，通过判断是否有  @UserLoginToken 注解 决定是否需要登录
    }

    @Bean
    fun authenticationInterceptor(): AuthenticationInterceptor {
        return AuthenticationInterceptor()
    }
}
