package com.btm.back.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration


/**
 * WebSocket配置类
 * 配置WebSocket端点和消息代理
 * @author Trae AI
 * @date 2023-06-01
 */
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    
    /**
     * 配置消息代理
     * @param registry 消息代理注册表
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic", "/queue")
        registry.setApplicationDestinationPrefixes("/app")
        registry.setUserDestinationPrefix("/user")
//        registry.configureClientInboundChannel { channel -> channel.taskExecutor().corePoolSize = 4 }
    }

    @Bean
    fun messageConverter(): MessageConverter = MappingJackson2MessageConverter()

    // 在WebSocketConfig中添加心跳
    override fun configureWebSocketTransport(registration: WebSocketTransportRegistration) {
        registration.setSendTimeLimit(15 * 1000)
            .setSendBufferSizeLimit(512 * 1024)
    }
    /**
     * 注册STOMP端点
     * @param registry STOMP端点注册表
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // 注册WebSocket端点，允许跨域访问
        registry.addEndpoint("/ws/ai-chat")
                .setAllowedOrigins("*")
//                .withSockJS() // 启用SockJS支持
//                .setMessageConverters(arrayOf(messageConverter()))


    }
}