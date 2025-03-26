package com.btm.back.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.btm.back.config.AIChatConfig
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
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
    
    /**
     * 发送聊天请求并处理流式响应
     * @param message 用户消息
     * @param onChunk 处理每个响应块的回调函数
     * @param onComplete 处理完成后的回调函数
     * @param onError 处理错误的回调函数
     */
    fun streamChat(
        message: String,
        onChunk: Consumer<String>,
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
            
            val streamOptions = JSONObject()
            streamOptions["include_usage"] = true
            requestBody["stream_options"] = streamOptions
            
            val messages = ArrayList<JSONObject>()
            val userMessage = JSONObject()
            userMessage["role"] = "user"
            userMessage["content"] = message
            messages.add(userMessage)
            requestBody["messages"] = messages
            
            // 设置请求体
            val entity = StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON)
            httpPost.entity = entity
            
            // 执行请求
            val response = httpClient.execute(httpPost)
            val statusCode = response.statusLine.statusCode
            
            if (statusCode == 200) {
                val entity: HttpEntity = response.entity
                val reader = BufferedReader(InputStreamReader(entity.content))
                
                val fullResponse = StringBuilder()
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
                                if (delta != null && delta.containsKey("content")) {
                                    val content = delta.getString("content")
                                    if (content != null && content.isNotEmpty()) {
                                        onChunk.accept(content)
                                        fullResponse.append(content)
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