package com.btm.back.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
    lateinit var secret: String
    var expiration: Long = 604800000 // 默认7天
    var refreshThreshold: Long = 86400000 // 默认1天，用于判断是否需要刷新token
} 