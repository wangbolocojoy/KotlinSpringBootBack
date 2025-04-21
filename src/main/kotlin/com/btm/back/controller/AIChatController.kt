package com.btm.back.controller

import com.btm.back.bean.ReqBody
import com.btm.back.imp.AIChatServiceImpl
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@Tag(name = "AI聊天", description = "AI聊天相关接口")
@RestController
@RequestMapping(value = ["/api/ai"])
class AIChatController {
    @Autowired
    lateinit var aiChatServiceImpl: AIChatServiceImpl
    
    /**
     * 处理文本聊天
     * @param reqBody 请求体，包含用户ID和消息内容
     * @return 响应体，包含AI回复内容
     */
    @Operation(summary = "文本聊天", description = "发送文本消息与AI聊天")
    @UserLoginToken
    @RequestMapping(value = ["chat/text"], method = [RequestMethod.POST])
    fun processTextChat(
        @Parameter(description = "聊天请求信息", required = true)
        @Valid @RequestBody reqBody: ReqBody
    ) = aiChatServiceImpl.processTextChat(reqBody)
    
    /**
     * 处理图像聊天
     * @param userId 用户ID
     * @param message 文本消息（可选）
     * @param imageFile 图像文件
     * @return 响应体，包含AI回复内容
     */
    @Operation(summary = "图像聊天", description = "发送图像与AI聊天")
    @UserLoginToken
    @RequestMapping(value = ["chat/image"], method = [RequestMethod.POST])
    fun processImageChat(
        @RequestParam("userId") userId: Int,
        @RequestParam("message", required = false, defaultValue = "") message: String,
        @RequestParam("imageFile") imageFile: MultipartFile?
    ) = aiChatServiceImpl.processImageChat(userId, message, imageFile)
    
    /**
     * 处理语音聊天
     * @param userId 用户ID
     * @param audioFile 语音文件
     * @return 响应体，包含AI回复内容
     */
    @Operation(summary = "语音聊天", description = "发送语音与AI聊天")
    @UserLoginToken
    @RequestMapping(value = ["chat/voice"], method = [RequestMethod.POST])
    fun processVoiceChat(
        @RequestParam("userId") userId: Int,
        @RequestParam("audioFile") audioFile: MultipartFile?
    ) = aiChatServiceImpl.processVoiceChat(userId, audioFile)
    
    /**
     * 获取聊天历史
     * @param reqBody 请求体，包含用户ID和分页信息
     * @return 响应体，包含聊天历史记录
     */
    @Operation(summary = "获取聊天历史", description = "获取用户的聊天历史记录")
    @UserLoginToken
    @RequestMapping(value = ["history"], method = [RequestMethod.POST])
    fun getChatHistory(
        @Parameter(description = "历史请求信息", required = true)
        @Valid @RequestBody reqBody: ReqBody
    ) = aiChatServiceImpl.getChatHistory(reqBody)

    /**
     * 清除聊天历史
     * @param reqBody 请求体，包含用户ID
     * @return 响应体，表示操作结果
     */
    @Operation(summary = "清除聊天历史", description = "清除用户的所有聊天历史")
    @UserLoginToken
    @RequestMapping(value = ["history/clear"], method = [RequestMethod.POST])
    fun clearChatHistory(
        @Parameter(description = "清除历史请求信息", required = true)
        @Valid @RequestBody reqBody: ReqBody
    ) = aiChatServiceImpl.clearChatHistory(reqBody)
}