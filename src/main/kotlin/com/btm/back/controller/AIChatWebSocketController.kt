package com.btm.back.controller

import com.btm.back.imp.AIChatServiceImpl
import com.btm.back.mapper.ChatHistoryMapper
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
    lateinit var chatHistoryMapper: ChatHistoryMapper

    @Autowired
    private lateinit var userMapper: UserMapper
    
    @Autowired
    private lateinit var dashScopeClient: DashScopeClient
    
    /**
     * 获取用户的聊天历史记录
     * @param userId 用户ID
     * @param limit 获取的历史记录数量限制
     * @return 聊天历史记录列表，每条记录包含用户消息和AI回复
     */
    private fun getUserChatHistory(userId: Int, limit: Int = 5): List<Pair<String, String>> {
        try {
            // 获取用户最近的聊天历史记录
            val chatHistory = chatHistoryMapper.findByUserIdOrderByCreateTimeDesc(userId, 0, limit)
            return chatHistory.map { Pair(it.userMessage, it.aiResponse) }
        } catch (e: Exception) {
            logger.error("获取聊天历史失败: ${e.message}", e)
            return emptyList()
        }
    }
    
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
            
            // 获取用户的聊天历史记录
            val chatHistory = getUserChatHistory(chatMessage.senderId, 5)
            logger.info("获取到${chatHistory.size}条聊天历史记录")
            
            // 创建响应消息ID
            val responseId = UUID.randomUUID().toString()
            val responseBuilder = StringBuilder()
            
            // 调用DashScope API处理文本消息，使用流式响应
            logger.info("开始调用DashScope API处理消息")
            dashScopeClient.streamChat(
                message = chatMessage.content,
                chatHistory = chatHistory,
                onChunk = { chunk ->
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
                onChunkAudio = { chunkAudio ->
//                    logger.info("收到AI语音块: $chunkAudio")
                    val chunkAudioMessage = ChatMessage(
                        id = responseId,
                        senderId = 1, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.VOICE,
                        content = chunkAudio,
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.READ
                    )
                    messagingTemplate.convertAndSend("/queue/chunks",
                        chunkAudioMessage)
                },
                onComplete = { fullResponse ->
                    logger.info("AI响应完成，完整消息${fullResponse}")
//                    // 创建完整的AI回复消息
                    val completeMessage = ChatMessage(
                        id = responseId,
                        senderId = 1, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.VOICE,
                        content = "",
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.END
                    )
////
//                    // 发送完整消息到用户
//                    messagingTemplate.convertAndSend(
//                        "/queue/messages",
//                        completeMessage
//                    )
//
//                    // 保存聊天历史
                   aiChatServiceImpl.saveChatHistory(
                       chatMessage.senderId,
                       chatMessage.content,
                       fullResponse,
                       "text"
                   )
//                    logger.info("聊天历史已保存")
                },
                onError = { error ->
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
        logger.info("收到图像消息: $chatMessage")
        
        // 验证用户
//        val user = userMapper.findById(chatMessage.senderId) ?: run {
//            sendErrorMessage(chatMessage.senderId, "用户不存在")
//            return
//        }
        
        // 处理图像消息
        try {
            // 发送一个初始消息，表示AI正在思考
            val thinkingMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = 0, // 0表示系统/AI发送
                receiverId = chatMessage.senderId,
                type = MessageType.SYSTEM,
                content = "AI正在分析图像...",
                timestamp = LocalDateTime.now().toString()
            )
            
            messagingTemplate.convertAndSendToUser(
                chatMessage.senderId.toString(),
                "/queue/messages",
                thinkingMessage
            )
            
            // 获取用户的聊天历史记录
            val chatHistory = getUserChatHistory(chatMessage.senderId, 5)
            logger.info("获取到${chatHistory.size}条聊天历史记录")
            
            // 创建响应消息ID
            val responseId = UUID.randomUUID().toString()
            val responseBuilder = StringBuilder()
            
            // 调用DashScope API处理图像消息，使用流式响应
            logger.info("开始调用DashScope API处理图像消息")
            dashScopeClient.streamChat(
                message = chatMessage.content,
                imageUrl = chatMessage.mediaUrl,
                chatHistory = chatHistory,
                onChunk = { chunk ->
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
                    messagingTemplate.convertAndSend("/queue/chunks", chunkMessage)
                    
                    // 累积响应内容
                    responseBuilder.append(chunk)
                },
                onChunkAudio = { chunkAudio ->
                    val chunkAudioMessage = ChatMessage(
                        id = responseId,
                        senderId = 0, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.VOICE,
                        content = chunkAudio,
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.READ
                    )
                    messagingTemplate.convertAndSend("/queue/chunks", chunkAudioMessage)
                },
                onComplete = { fullResponse ->
                    logger.info("AI响应完成，完整消息${fullResponse}")
                    val completeMessage = ChatMessage(
                        id = responseId,
                        senderId = 0, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.VOICE,
                        content = "",
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.END
                    )
                    
                    // 发送完整消息到用户
                    messagingTemplate.convertAndSend(
                        "/queue/messages",
                        completeMessage
                    )
                    
                    // 保存聊天历史
                    aiChatServiceImpl.saveChatHistory(
                        chatMessage.senderId,
                        "[图像] ${chatMessage.content}",
                        fullResponse,
                        "image",
                        chatMessage.mediaUrl
                    )
                },
                onError = { error ->
                    logger.error("处理图像消息失败: ${error.message}", error)
                    sendErrorMessage(chatMessage.senderId, "处理图像消息失败: ${error.message}")
                }
            )
        } catch (e: Exception) {
            logger.error("处理图像消息时发生异常: ${e.message}", e)
            sendErrorMessage(chatMessage.senderId, "处理图像消息失败: ${e.message}")
        }
    }
    
    /**
     * 处理语音消息
     * @param chatMessage 聊天消息
     */
    @MessageMapping("/chat.sendVoice")
    fun handleVoiceMessage(@Payload chatMessage: ChatMessage) {
        logger.info("收到语音消息: $chatMessage")
        
        // 验证用户
//        val user = userMapper.findById(chatMessage.senderId) ?: run {
//            sendErrorMessage(chatMessage.senderId.toInt(), "用户不存在")
//            return
//        }
    
        // 处理语音消息
        try {
            // 发送一个初始消息，表示AI正在思考
            val thinkingMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = 0, // 0表示系统/AI发送
                receiverId = chatMessage.senderId,
                type = MessageType.SYSTEM,
                content = "AI正在处理语音...",
                timestamp = LocalDateTime.now().toString()
            )
            
            messagingTemplate.convertAndSendToUser(
                chatMessage.senderId.toString(),
                "/queue/messages",
                thinkingMessage
            )
            
            // 获取用户的聊天历史记录
            val chatHistory = getUserChatHistory(chatMessage.senderId, 5)
            logger.info("获取到${chatHistory.size}条聊天历史记录")
            
            // 创建响应消息ID
            val responseId = UUID.randomUUID().toString()
            val responseBuilder = StringBuilder()
            
            // 调用DashScope API处理语音消息，使用流式响应
            logger.info("开始调用DashScope API处理语音消息")
            dashScopeClient.streamChat(
                message = chatMessage.content ?: "",  // 可能没有文本内容
                audioUrl = chatMessage.mediaUrl,
                chatHistory = chatHistory,
                onChunk = { chunk ->
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
                    messagingTemplate.convertAndSend("/queue/chunks", chunkMessage)
                    
                    // 累积响应内容
                    responseBuilder.append(chunk)
                },
                onChunkAudio = { chunkAudio ->
                    val chunkAudioMessage = ChatMessage(
                        id = responseId,
                        senderId = 0, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.VOICE,
                        content = chunkAudio,
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.READ
                    )
                    messagingTemplate.convertAndSend("/queue/chunks", chunkAudioMessage)
                },
                onComplete = { fullResponse ->
                    logger.info("AI响应完成，完整消息${fullResponse}")
                    val completeMessage = ChatMessage(
                        id = responseId,
                        senderId = 0, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.VOICE,
                        content = "",
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.END
                    )
                    
                    // 发送完整消息到用户
                    messagingTemplate.convertAndSend(
                        "/queue/messages",
                        completeMessage
                    )
                    
                    // 保存聊天历史
                    aiChatServiceImpl.saveChatHistory(
                        chatMessage.senderId,
                        "[语音] ${chatMessage.content ?: ""}",
                        fullResponse,
                        "voice",
                        chatMessage.mediaUrl
                    )
                },
                onError = { error ->
                    logger.error("处理语音消息失败: ${error.message}", error)
                    sendErrorMessage(chatMessage.senderId, "处理语音消息失败: ${error.message}")
                }
            )
        } catch (e: Exception) {
            logger.error("处理语音消息时发生异常: ${e.message}", e)
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
    
    /**
     * 处理视频消息
     * @param chatMessage 聊天消息
     */
    @MessageMapping("/chat.sendVideo")
    fun handleVideoMessage(@Payload chatMessage: ChatMessage) {
        logger.info("收到视频消息: $chatMessage")
        
        // 验证用户
//        val user = userMapper.findById(chatMessage.senderId) ?: run {
//            sendErrorMessage(chatMessage.senderId, "用户不存在")
//            return
//        }
        
        // 处理视频消息
        try {
            // 发送一个初始消息，表示AI正在思考
            val thinkingMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = 0, // 0表示系统/AI发送
                receiverId = chatMessage.senderId,
                type = MessageType.SYSTEM,
                content = "AI正在分析视频...",
                timestamp = LocalDateTime.now().toString()
            )
            
            messagingTemplate.convertAndSendToUser(
                chatMessage.senderId.toString(),
                "/queue/messages",
                thinkingMessage
            )
            
            // 获取用户的聊天历史记录
            val chatHistory = getUserChatHistory(chatMessage.senderId, 5)
            logger.info("获取到${chatHistory.size}条聊天历史记录")
            
            // 创建响应消息ID
            val responseId = UUID.randomUUID().toString()
            val responseBuilder = StringBuilder()
            
            // 解析视频帧URL列表
            val videoUrls = chatMessage.mediaUrl?.split(",") ?: emptyList()
            
            // 调用DashScope API处理视频消息，使用流式响应
            logger.info("开始调用DashScope API处理视频消息")
            dashScopeClient.streamChat(
                message = chatMessage.content,
                videoUrls = videoUrls,
                chatHistory = chatHistory,
                onChunk = { chunk ->
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
                    messagingTemplate.convertAndSend("/queue/chunks", chunkMessage)
                    
                    // 累积响应内容
                    responseBuilder.append(chunk)
                },
                onChunkAudio = { chunkAudio ->
                    val chunkAudioMessage = ChatMessage(
                        id = responseId,
                        senderId = 0, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.VOICE,
                        content = chunkAudio,
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.READ
                    )
                    messagingTemplate.convertAndSend("/queue/chunks", chunkAudioMessage)
                },
                onComplete = { fullResponse ->
                    logger.info("AI响应完成，完整消息${fullResponse}")
                    val completeMessage = ChatMessage(
                        id = responseId,
                        senderId = 0, // 0表示系统/AI发送
                        receiverId = chatMessage.senderId,
                        type = MessageType.VOICE,
                        content = "",
                        timestamp = LocalDateTime.now().toString(),
                        status = MessageStatus.END
                    )
                    
                    // 发送完整消息到用户
                    messagingTemplate.convertAndSend(
                        "/queue/messages",
                        completeMessage
                    )
                    
                    // 保存聊天历史
                    aiChatServiceImpl.saveChatHistory(
                        chatMessage.senderId,
                        "[视频] ${chatMessage.content}",
                        fullResponse,
                        "video",
                        chatMessage.mediaUrl
                    )
                },
                onError = { error ->
                    logger.error("处理视频消息失败: ${error.message}", error)
                    sendErrorMessage(chatMessage.senderId, "处理视频消息失败: ${error.message}")
                }
            )
        } catch (e: Exception) {
            logger.error("处理视频消息时发生异常: ${e.message}", e)
            sendErrorMessage(chatMessage.senderId, "处理视频消息失败: ${e.message}")
        }
    }
    
    // 这些方法不再需要，因为我们直接使用DashScopeClient处理多模态输入
    // 保留这些方法的空实现，以防其他地方有引用
    private fun processAIImageResponse(content: String, mediaUrl: String?, userId: Int): String {
        return "AI回复：已处理图像消息"
    }

    private fun processAIVoiceResponse(audioUrl: String?, userId: Long): String {
        return "AI回复：已处理语音消息"
    }
}