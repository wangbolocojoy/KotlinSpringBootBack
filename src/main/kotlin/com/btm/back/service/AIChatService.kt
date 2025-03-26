package com.btm.back.service

import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult
import org.springframework.web.multipart.MultipartFile

/**
 * AI聊天服务接口
 * @author Trae AI
 * @date 2023-06-01
 */
interface AIChatService {
    
    /**
     * 处理文本聊天
     * @param reqBody 请求体，包含用户ID和消息内容
     * @return 响应体，包含AI回复内容
     */
    fun processTextChat(reqBody: ReqBody): BaseResult
    
    /**
     * 处理图像聊天
     * @param userId 用户ID
     * @param message 文本消息（可选）
     * @param imageFile 图像文件
     * @return 响应体，包含AI回复内容
     */
    fun processImageChat(userId: Int, message: String, imageFile: MultipartFile?): BaseResult
    
    /**
     * 处理语音聊天
     * @param userId 用户ID
     * @param audioFile 语音文件
     * @return 响应体，包含AI回复内容
     */
    fun processVoiceChat(userId: Int, audioFile: MultipartFile?): BaseResult
    
    /**
     * 获取聊天历史
     * @param reqBody 请求体，包含用户ID和分页信息
     * @return 响应体，包含聊天历史记录
     */
    fun getChatHistory(reqBody: ReqBody): BaseResult
    
    /**
     * 清除聊天历史
     * @param reqBody 请求体，包含用户ID
     * @return 响应体，表示操作结果
     */
    fun clearChatHistory(reqBody: ReqBody): BaseResult
}