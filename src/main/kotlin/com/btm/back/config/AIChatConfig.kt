package com.btm.back.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

/**
 * AI聊天配置类
 * 配置AI API相关参数
 * @author Trae AI
 * @date 2023-06-01
 */
@Configuration
@Component
class AIChatConfig {
    
    /**
     * 阿里云DashScope API密钥
     */
    @Value("\${ai.dashscope.api-key:}")
    lateinit var dashScopeApiKey: String
    
    /**
     * 阿里云DashScope API URL
     */
    @Value("\${ai.dashscope.api-url:https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions}")
    lateinit var dashScopeApiUrl: String
    
    /**
     * 阿里云DashScope 模型名称
     */
    @Value("\${ai.dashscope.model:qwq-32b}")
    lateinit var dashScopeModel: String
}