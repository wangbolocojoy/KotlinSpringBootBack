package com.btm.back.vo

import java.time.LocalDateTime
import javax.persistence.*

/**
 * 聊天历史数据模型
 * @author Trae AI
 * @date 2023-06-01
 */
@Entity
@Table(name = "chat_history")
data class ChatHistoryVO(
    @Id
    val id: String,
    
    @Column(name = "user_id", nullable = false)
    val userId: Int,
    
    @Column(name = "user_message", nullable = false, columnDefinition = "TEXT")
    val userMessage: String,
    
    @Column(name = "ai_response", nullable = false, columnDefinition = "TEXT")
    val aiResponse: String,
    
    @Column(name = "message_type", nullable = false)
    val messageType: String, // text, image, voice
    
    @Column(name = "media_path")
    val mediaPath: String? = null,
    
    @Column(name = "create_time", nullable = false)
    val createTime: LocalDateTime
)