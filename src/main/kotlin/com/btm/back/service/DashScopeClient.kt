package com.btm.back.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.btm.back.config.AIChatConfig
import com.btm.back.controller.AIChatWebSocketController
import com.fasterxml.jackson.module.kotlin.jsonMapper
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.function.Consumer

/**
 * DashScope客户端
 * 用于与阿里云DashScope API交互
 * @author Trae AI
 * @date 2023-06-01
 */
@Service
class DashScopeClient {
    
    @Autowired
    private lateinit var aiChatConfig: AIChatConfig
    private val logger = LoggerFactory.getLogger(AIChatWebSocketController::class.java)
    /**
     * 发送聊天请求并处理流式响应
     * @param message 用户消息
     * @param imageUrl 图片URL（可选）
     * @param audioUrl 音频URL（可选）
     * @param chatHistory 聊天历史记录（可选）
     * @param onChunk 处理每个响应块的回调函数
     * @param onChunkAudio 处理每个音频响应块的回调函数
     * @param onComplete 处理完成后的回调函数
     * @param onError 处理错误的回调函数
     */
    fun streamChat(
        message: String,
        imageUrl: String? = null,
        audioUrl: String? = null,
        videoUrls: List<String>? = null,
        chatHistory: List<Pair<String, String>>? = null,
        onChunk: Consumer<String>,
        onChunkAudio: Consumer<String>,
        onComplete: Consumer<String>,
        onError: Consumer<Exception>
    ) {
        try {
            val httpClient = HttpClients.createDefault()
            val httpPost = HttpPost(aiChatConfig.dashScopeApiUrl)
            
            // 设置请求头
            httpPost.setHeader("Authorization", "Bearer ${aiChatConfig.dashScopeApiKey}")
            httpPost.setHeader("Content-Type", "application/json")
            
            // 构建请求体
            val requestBody = JSONObject()
            requestBody["model"] = aiChatConfig.dashScopeModel
            requestBody["stream"] = true
            requestBody["modalities"] = arrayOf("text", "audio")
            val audioConfig = mapOf("voice" to "Chelsie", "format" to "wav")
            requestBody["audio"] = audioConfig
            
            val streamOptions = JSONObject()
            streamOptions["include_usage"] = true
            requestBody["stream_options"] = streamOptions
            
            // 构建消息数组
            val messages = ArrayList<JSONObject>()
            
            // 添加系统消息
            val systemmessagecontent = "你叫宁姚 这是你的角色设定 宁姚角色设定\n" +
                    "\n" +
                    "【基础信息】\n" +
                    "姓名：宁姚\n" +
                    "性别：女\n" +
                    "年龄：18岁（初登场）\n" +
                    "身份：剑气长城宁氏嫡女/陈平安道侣/飞升境剑修\n" +
                    "外貌特征：身姿高挑如剑脊，黛眉凌厉似剑锋，琥珀色瞳孔流转剑气寒光，常年身着素白剑袍，腰间悬古剑\"天真\"，发间别梧桐叶状剑簪。\n" +
                    "\n" +
                    "【核心特质】\n" +
                    "\n" +
                    "剑道天赋：先天剑胚体质，九岁握剑通灵，十三岁自创\"斩龙式\"，剑气自带青冥色光晕，战斗风格兼具古剑修的杀伐果决与新生代的剑意灵动。\n" +
                    "\n" +
                    "性格矛盾体：表面冷傲如万年玄冰，实则重情重义如熔岩暗涌。对敌时剑出无情，面对陈平安时会不自觉地抿唇垂眸，暴露出少女特有的执拗与笨拙温柔。\n" +
                    "\n" +
                    "命运羁绊：背负剑气长城守将世家的千年宿命，却在骊珠洞天与陈平安产生因果纠缠。本命飞剑\"照胆\"映照道心，剑身裂纹象征情感与责任的永恒博弈。\n" +
                    "\n" +
                    "【成长脉络】\n" +
                    "幼年时期：在剑气长城烽火中成长，目睹母亲战死后将情感冰封，以\"宁氏剑修只需向死而生\"为生存信条。\n" +
                    "\n" +
                    "情感觉醒：初次相遇时为救陈平安破例动用本命精血，后于倒悬山重逢时领悟\"向死而生亦可向生而死\"的剑道真谛，剑气由青转金。\n" +
                    "\n" +
                    "巅峰时刻：独守城头三日斩杀十二头王座大妖，战后破碎本命剑\"天真\"重铸为双剑\"敢死\"与\"敢活\"，标志着重杀戮到守苍生的道心蜕变。\n" +
                    "\n" +
                    "【人物关系锚点】\n" +
                    "与陈平安：从\"累赘论\"到生死相托，两人以半枚铜钱为信物，开创\"剑气长河绕青山\"的双修剑阵。\n" +
                    "\n" +
                    "与齐廷济：亦师亦敌，继承其\"宁折不弯\"剑道却走出更决绝的\"宁断不弯\"之路。\n" +
                    "\n" +
                    "与陆芝：镜像对照，同为女子剑仙却选择截然不同的证道方式，暗藏剑气长城新旧两代传承隐喻。\n" +
                    "\n" +
                    "【标志性符号】\n" +
                    "• 梧桐剑簪：母亲遗物，封印着宁氏初代剑祖三道保命剑气\n" +
                    "• 剑鞘刻痕：每救陈平安一次便添新痕，最终形成\"平安\"二字剑纹\n" +
                    "• 战后习惯：总在无人处将断发编成剑穗，后成为新生代剑修效仿的风尚\n" +
                    "\n" +
                    "此设定突出宁姚作为传统剑修世家继承者与新时代证道者的双重身份，通过器物细节与行为特征强化其外冷内热的角色魅力"
            val systemmessage = JSONObject()
            systemmessage["role"] = "system"
            systemmessage["content"] = systemmessagecontent
            messages.add(systemmessage)
            
            // 添加历史消息
            chatHistory?.forEach { (userMsg, aiMsg) ->
                val historyUserMessage = JSONObject()
                historyUserMessage["role"] = "user"
                historyUserMessage["content"] = userMsg
                messages.add(historyUserMessage)
                
                val historyAiMessage = JSONObject()
                historyAiMessage["role"] = "assistant"
                historyAiMessage["content"] = aiMsg
                messages.add(historyAiMessage)
            }
            
            // 添加当前用户消息
            val userMessage = JSONObject()
            userMessage["role"] = "user"
            
            // 处理多模态输入
            if (imageUrl != null || audioUrl != null || videoUrls != null) {
                // 使用数组存储多模态内容
                val contentArray = ArrayList<JSONObject>()
                
                // 添加文本内容
                if (message.isNotEmpty()) {
                    val textContent = JSONObject()
                    textContent["type"] = "text"
                    textContent["text"] = message
                    contentArray.add(textContent)
                }
                
                // 添加图片内容
                if (imageUrl != null) {
                    val imageContent = JSONObject()
                    imageContent["type"] = "image_url"
                    val imageUrlObj = JSONObject()
                    imageUrlObj["url"] = imageUrl
                    imageContent["image_url"] = imageUrlObj
                    contentArray.add(imageContent)
                }
                
                // 添加音频内容
                if (audioUrl != null) {
                    val audioContent = JSONObject()
                    audioContent["type"] = "input_audio"
                    val audioUrlObj = JSONObject()
                    audioUrlObj["data"] = audioUrl
                    audioContent["input_audio"] = audioUrlObj
                    contentArray.add(audioContent)
                }
                
                // 添加视频内容（以图片帧序列形式）
                if (videoUrls != null && videoUrls.isNotEmpty()) {
                    val videoContent = JSONObject()
                    videoContent["type"] = "video"
                    val videoFrames = ArrayList<String>()
                    
                    // 添加所有视频帧URL
                    videoUrls.forEach { url ->
                        videoFrames.add(url)
                    }
                    
                    videoContent["video"] = videoFrames
                    contentArray.add(videoContent)
                }
                
                userMessage["content"] = contentArray
            } else {
                // 纯文本消息
                userMessage["content"] = message
            }
            
            messages.add(userMessage)
            requestBody["messages"] = messages
            
            // 设置请求体
            val entity = StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON)
            httpPost.entity = entity
            
            // 执行请求
            logger.info("// 请求体${requestBody.toJSONString()}")
            val response = httpClient.execute(httpPost)
            val statusCode = response.statusLine.statusCode
            
            if (statusCode == 200) {
                val entity: HttpEntity = response.entity
                val reader = BufferedReader(InputStreamReader(entity.content))
                
                val fullResponse = StringBuilder()
                val audioData = StringBuilder() // 用于存储音频数据
                var line: String?
                
                // 处理流式响应
                while (reader.readLine().also { line = it } != null) {
                    if (line!!.startsWith("data: ")) {
                        val jsonData = line!!.substring(6) // 去掉 "data: "
                        
                        if (jsonData == "[DONE]") {
                            // 流结束
                            break
                        }
                        
                        try {
                            val responseObj = JSON.parseObject(jsonData)
                            val choices = responseObj.getJSONArray("choices")
                            if (choices != null && choices.size > 0) {
                                val choice = choices.getJSONObject(0)
                                val delta = choice.getJSONObject("delta")
                                if (delta != null) {
                                    if (delta.containsKey("content")) {
                                        val content = delta.getString("content")
                                        if (content != null && content.isNotEmpty()) {
                                            onChunk.accept(content)
                                            fullResponse.append(content)
                                        }
                                    } else if (delta.containsKey("audio")) {
                                        val audio = delta.getJSONObject("audio")
                                        val transcript = audio.getString("transcript")
                                        val data = audio.getString("data")
                                        if (transcript != null && transcript.isNotEmpty()) {
                                            onChunk.accept(transcript)
                                            fullResponse.append(transcript)
                                        }
                                        if (data != null && data.isNotEmpty()) {
                                            onChunkAudio.accept(data)
                                            audioData.append(data)
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // 忽略解析错误，继续处理下一行
                        }
                    }
                }
                
                // 完成回调
                onComplete.accept(fullResponse.toString())
                
                // 将音频数据写入文件
//                if (audioData.isNotEmpty()) {
//                    val audioBytes = java.util.Base64.getDecoder().decode(audioData.toString())
//                    val audioFile = java.io.File("output.wav")
//                    java.io.FileOutputStream(audioFile).use { fos ->
//                        fos.write(audioBytes)
//                    }
////                    onComplete.accept(audioFile)
//                }
            } else {
                // 处理错误响应
                val errorContent = EntityUtils.toString(response.entity)
                onError.accept(Exception("API请求失败: $statusCode, $errorContent"))
            }
            
            // 关闭连接
            httpClient.close()
        } catch (e: Exception) {
            onError.accept(e)
        }
    }
}