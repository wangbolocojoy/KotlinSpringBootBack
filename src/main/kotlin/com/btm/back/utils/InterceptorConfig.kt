package com.btm.back.utils

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**") // 拦截所有请求，通过判断是否有  @UserLoginToken 注解 决定是否需要登录
                .excludePathPatterns(
                    "/swagger-resources/**", 
                    "/webjars/**", 
                    "/v2/**", 
                    "/v3/**", 
                    "/swagger-ui.html/**", 
                    "/swagger-ui/**"
                ) // 不拦截Swagger相关资源
    }
    
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // 旧版Swagger UI资源处理
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")
        // 新版Swagger UI资源处理
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.15.5/")
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
        super.addResourceHandlers(registry)
    }

    @Bean
    fun authenticationInterceptor(): AuthenticationInterceptor {
        return AuthenticationInterceptor()
    }
}
