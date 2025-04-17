package com.btm.back.model

import java.time.LocalDateTime
import java.util.*

/**
 * 聊天消息模型
 * 用于WebSocket通信中的消息传递
 * @author Trae AI
 * @date 2023-06-01
 */
data class ChatMessage(
    // 消息ID
    val id: String = "5",
    
    // 发送者ID（用户ID或系统ID）
    val senderId: Int,
    
    // 接收者ID（用户ID或系统ID）
    val receiverId: Int? = null,
    
    // 消息类型：text, image, voice, video
    val type: MessageType,
    
    // 消息内容
    val content: String,
    
    // 媒体文件URL（图片、语音、视频等）
    val mediaUrl: String? = null,
    
    // 消息发送时间
    val timestamp: String = LocalDateTime.now().toString(),
    
    // 消息状态：sent, delivered, read
    val status: MessageStatus = MessageStatus.SENT,

    val isFromUser: Boolean = false
)

/**
 * 消息类型枚举
 */
enum class MessageType {
    TEXT,       // 文本消息
    IMAGE,      // 图片消息
    VOICE,      // 语音消息
    VIDEO,      // 视频消息
    FILE,       // 文件消息
    SYSTEM,      // 系统消息
    ERROR      // 错误消息
}

/**
 * 消息状态枚举
 */
enum class MessageStatus {
    SENT,       // 已发送
    DELIVERED,  // 已送达
    READ,        // 已读
    END
}