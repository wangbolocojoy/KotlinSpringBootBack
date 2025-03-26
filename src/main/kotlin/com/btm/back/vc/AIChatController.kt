package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.AIChatServiceImpl
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@Tag(name = "AI多模态聊天", description = "AI多模态聊天相关接口，支持文本、图像等多种输入方式")
@RestController
@RequestMapping(value = ["/api/AIChat"])
class MultiModalAIChatController {
    
    @Autowired
    lateinit var aiChatServiceImpl: AIChatServiceImpl
    
    @Operation(summary = "文本聊天", description = "发送文本消息与AI进行对话")
    @PassToken
    @RequestMapping(value = ["textChat"], method = [RequestMethod.POST])
    fun textChat(
        @Parameter(description = "聊天请求信息", required = true)
        @Valid @RequestBody reqBody: ReqBody
    ) = aiChatServiceImpl.processTextChat(reqBody)
    
    @Operation(summary = "图像聊天", description = "上传图像与AI进行对话，AI可以理解图像内容")
    @PassToken
    @RequestMapping(value = ["imageChat"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun imageChat(
        @RequestParam userId: Int,
        @RequestParam message: String,
        @RequestPart("imageFile") imageFile: MultipartFile?
    ) = aiChatServiceImpl.processImageChat(userId, message, imageFile)
    
    @Operation(summary = "语音聊天", description = "上传语音与AI进行对话，AI可以理解语音内容并回复")
    @PassToken
    @RequestMapping(value = ["voiceChat"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun voiceChat(
        @RequestParam userId: Int,
        @RequestPart("audioFile") audioFile: MultipartFile?
    ) = aiChatServiceImpl.processVoiceChat(userId, audioFile)
    
    @Operation(summary = "获取聊天历史", description = "获取用户与AI的聊天历史记录")
    @PassToken
    @RequestMapping(value = ["getChatHistory"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun getChatHistory(@Valid @RequestBody reqBody: ReqBody) =
        aiChatServiceImpl.getChatHistory(reqBody)
    
    @Operation(summary = "清除聊天历史", description = "清除用户与AI的聊天历史记录")
    @PassToken
    @RequestMapping(value = ["clearChatHistory"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun clearChatHistory(@Valid @RequestBody reqBody: ReqBody) = 
        aiChatServiceImpl.clearChatHistory(reqBody)
}