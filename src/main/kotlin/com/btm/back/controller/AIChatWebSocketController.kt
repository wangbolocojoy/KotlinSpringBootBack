package com.btm.back.controller

import com.btm.back.imp.AIChatServiceImpl
import com.btm.back.model.ChatMessage
import com.btm.back.model.MessageType
import com.btm.back.model.MessageStatus
import com.btm.back.mapper.UserMapper
import com.btm.back.service.DashScopeClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import org.slf4j.LoggerFactory

/**
 * AI聊天WebSocket控制器
 * 处理WebSocket连接和消息
 * @author Trae AI
 * @date 2023-06-01
 */
@Controller
class AIChatWebSocketController {
    
    private val logger = LoggerFactory.getLogger(AIChatWebSocketController::class.java)
    
    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate
    
    @Autowired
    private lateinit var aiChatServiceImpl: AIChatServiceImpl
    
    @Autowired
    private lateinit var userMapper: UserMapper
    
    @Autowired
    private lateinit var dashScopeClient: DashScopeClient
    
    /**
     * 处理文本消息
     * @param chatMessage 聊天消息
     */
    @MessageMapping("/chat.send")
    fun handleTextMessage(@Payload chatMessage: ChatMessage) {
        logger.info("收到文本消息: $chatMessage")
        
        // 验证用户
//        val user = userMapper.findById(5) ?: run {
//            logger.error("用户不存在: ${chatMessage.senderId}")
//            sendErrorMessage(chatMessage.senderId, "用户不存在")
//            return
//        }
        
        // 处理文本消息
        try {
            // 发送一个初始消息，表示AI正在思考
            val thinkingMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = 0, // 0表示系统/AI发送
                receiverId = chatMessage.senderId,
                type = MessageType.SYSTEM,
                content = "AI正在思考中...",
                timestamp = LocalDateTime.now().toString()
            )
            
            logger.info("发送思考中消息: $thinkingMessage")
            messagingTemplate.convertAndSendToUser(
                chatMessage.senderId.toString(),
                "/queue/messages",
                thinkingMessage
            )
            
            // 创建响应消息ID
            val responseId = UUID.randomUUID().toString()
            val responseBuilder = StringBuilder()
            
            // 调用DashScope API处理文本消息，使用流式响应
            logger.info("开始调用DashScope API处理消息")
            dashScopeClient.streamChat(
                chatMessage.content,
                // 处理每个响应块
                { chunk ->
                    logger.info("收到AI响应块: $chunk")
                    // 创建AI回复消息片段
                    val chunkMessage = ChatMessage(
                        id = responseId,
                        senderId = 1, // 0表示系统/1AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.TEXT,
                        content = chunk,
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.DELIVERED
                    )
                    
                    // 发送消息片段到用户
                    messagingTemplate.convertAndSendToUser(
                        chatMessage.senderId.toString(),
                        "/queue/chunks",
                        chunkMessage
                    )
                    // 发送消息片段
                    messagingTemplate.convertAndSend("/queue/chunks",
                    chunkMessage)
                    
                    // 累积响应内容
                    responseBuilder.append(chunk)
                },
                // 处理完成回调
                { fullResponse ->
                    logger.info("AI响应完成，发送完整消息")
                    // 创建完整的AI回复消息
                    val completeMessage = ChatMessage(
                        id = responseId,
                        senderId = 0, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.TEXT,
                        content = fullResponse,
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.READ
                    )
                    
                    // 发送完整消息到用户
                    messagingTemplate.convertAndSendToUser(
                        chatMessage.senderId.toString(),
                        "/queue/messages",
                        completeMessage
                    )
                    
                    // 保存聊天历史
                    aiChatServiceImpl.saveChatHistory(
                        chatMessage.senderId,
                        chatMessage.content,
                        fullResponse,
                        "text"
                    )
                    logger.info("聊天历史已保存")
                },
                // 处理错误回调
                { error ->
                    logger.error("处理消息失败: ${error.message}", error)
                    sendErrorMessage(chatMessage.senderId, "处理消息失败: ${error.message}")
                }
            )
        } catch (e: Exception) {
            logger.error("处理消息时发生异常: ${e.message}", e)
            sendErrorMessage(chatMessage.senderId, "处理消息失败: ${e.message}")
        }
    }
    
    /**
     * 处理图像消息
     * @param chatMessage 聊天消息
     */
    @MessageMapping("/chat.sendImage")
    fun handleImageMessage(@Payload chatMessage: ChatMessage) {
        // 验证用户
//        val user = userMapper.findById(chatMessage.senderId) ?: run {
//            sendErrorMessage(chatMessage.senderId, "用户不存在")
//            return
//        }
        
        // 处理图像消息
        try {
            // 这里需要处理图像URL，从chatMessage.mediaUrl获取图像
            // 实际实现中，可能需要下载图像或直接使用URL
            
            // 调用AI服务处理图像消息
            val aiResponse = processAIImageResponse(chatMessage.content, chatMessage.mediaUrl, chatMessage.senderId)
            
            // 创建AI回复消息
            val responseMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = 0, // 0表示系统/AI发送
                receiverId = chatMessage.senderId,
                type = MessageType.TEXT, // AI回复通常是文本
                content = aiResponse,
                timestamp = LocalDateTime.now().toString()
            )
            
            // 发送消息到用户
            messagingTemplate.convertAndSendToUser(
                chatMessage.senderId.toString(),
                "/queue/messages",
                responseMessage
            )
            
            // 保存聊天历史
            aiChatServiceImpl.saveChatHistory(
                chatMessage.senderId,
                "[图像] ${chatMessage.content}",
                aiResponse,
                "image",
                chatMessage.mediaUrl
            )
        } catch (e: Exception) {
            sendErrorMessage(chatMessage.senderId, "处理图像消息失败: ${e.message}")
        }
    }
    
    /**
     * 处理语音消息
     * @param chatMessage 聊天消息
     */
    @MessageMapping("/chat.sendVoice")
    fun handleVoiceMessage(@Payload chatMessage: ChatMessage) {
        // 验证用户
//        val user = userMapper.findById(chatMessage.senderId) ?: run {
//            sendErrorMessage(chatMessage.senderId.toInt(), "用户不存在")
//            return
//        }
    
        // 处理语音消息
        try {
            val aiResponse = processAIVoiceResponse(chatMessage.mediaUrl, chatMessage.senderId.toLong())
    
            // 创建AI回复消息
            val responseMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = 0, // 0表示系统/AI发送
                receiverId = chatMessage.senderId,
                type = MessageType.TEXT, // AI回复通常是文本
                content = aiResponse,
                timestamp = LocalDateTime.now().toString()
            )
            
            // 发送消息到用户
            messagingTemplate.convertAndSendToUser(
                chatMessage.senderId.toString(),
                "/queue/messages",
                responseMessage
            )
            
            // 保存聊天历史
            val transcribedText = ""
            aiChatServiceImpl.saveChatHistory(
                chatMessage.senderId,
                "[语音] $transcribedText",
                aiResponse,
                "voice",
                chatMessage.mediaUrl
            )
        } catch (e: Exception) {
            sendErrorMessage(chatMessage.senderId, "处理语音消息失败: ${e.message}")
        }
    }
    
    /**
     * 发送错误消息
     * @param userId 用户ID
     * @param errorMessage 错误消息
     */
    private fun sendErrorMessage(userId: Int, errorMessage: String) {
        val errorChatMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = 0, // 0表示系统/AI发送
            receiverId = userId,
            type = MessageType.ERROR,
            content = errorMessage,
            timestamp = LocalDateTime.now().toString()
        )
        messagingTemplate.convertAndSendToUser(
            userId.toString(),
            "/queue/errors",
            errorChatMessage
        )
    }
    
    private fun processAIImageResponse(content: String, mediaUrl: String?, userId: Int): String {
        // 处理图像消息逻辑
        return "AI回复：已处理图像消息"
    }

    private fun processAIVoiceResponse(audioUrl: String?, userId: Long): String {
        // 处理语音消息逻辑
        return "AI回复：已处理语音消息"
    }
}