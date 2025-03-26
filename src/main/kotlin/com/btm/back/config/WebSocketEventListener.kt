package com.btm.back.config

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent

/**
 * WebSocket事件监听器
 * 用于监控WebSocket连接的生命周期事件
 * @author Trae AI
 * @date 2023-06-01
 */
@Component
class WebSocketEventListener {
    private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    /**
     * 处理WebSocket连接事件
     * @param event 连接事件
     */
    @EventListener
    fun handleWebSocketConnect(event: SessionConnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId
        val user = accessor.user
        logger.info("WebSocket连接请求 - 会话ID: {}, 用户: {}", sessionId, user?.name ?: "未认证用户")
    }

    /**
     * 处理WebSocket连接成功事件
     * @param event 连接成功事件
     */
    @EventListener
    fun handleWebSocketConnected(event: SessionConnectedEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId
        logger.info("WebSocket连接成功 - 会话ID: {}", sessionId)
    }

    /**
     * 处理WebSocket订阅事件
     * @param event 订阅事件
     */
    @EventListener
    fun handleWebSocketSubscribe(event: SessionSubscribeEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId
        val destination = accessor.destination
        logger.info("WebSocket订阅 - 会话ID: {}, 订阅目标: {}", sessionId, destination)
    }

    /**
     * 处理WebSocket断开连接事件
     * @param event 断开连接事件
     */
    @EventListener
    fun handleWebSocketDisconnect(event: SessionDisconnectEvent) {
        val accessor = StompHeaderAccessor.wrap(event.message)
        val sessionId = accessor.sessionId
        val user = accessor.user
        logger.info("WebSocket断开连接 - 会话ID: {}, 用户: {}", sessionId, user?.name ?: "未认证用户")
    }
}