package com.btm.back.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.btm.back.config.JwtConfig
import com.btm.back.dto.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service("TokenService")
open class TokenService {
    @Autowired
    private lateinit var jwtConfig: JwtConfig
    
    @Autowired
    private lateinit var redisTemplate: StringRedisTemplate
    
    // Token在Redis中的前缀
    private val TOKEN_PREFIX = "user:token:"
    
    open fun getToken(user: User): String {
        // 生成JWT token
        val token = JWT.create()
            .withAudience(user.phone) // 将 user phone 保存到 token 里面
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.expiration))
            .sign(Algorithm.HMAC256(jwtConfig.secret))
        
        // 将token存储到Redis，以便后续验证和刷新
        // 键格式：user:token:{phone}，值为token
        redisTemplate.opsForValue().set(
            "$TOKEN_PREFIX${user.phone}", 
            token, 
            jwtConfig.expiration, 
            TimeUnit.MILLISECONDS
        )
        
        return token
    }
    
    /**
     * 检查token是否需要刷新
     * @param phone 用户手机号
     * @param token 当前token
     * @return 如果需要刷新，返回新token；否则返回null
     */
    open fun checkAndRefreshToken(phone: String, token: String): String? {
        // 从Redis获取存储的token
        val storedToken = redisTemplate.opsForValue().get("$TOKEN_PREFIX$phone") ?: return null
        
        // 如果传入的token与存储的不匹配，可能是无效token或者已更新
        if (token != storedToken) {
            return null
        }
        
        try {
            // 解析token
            val decodedJWT = JWT.require(Algorithm.HMAC256(jwtConfig.secret)).build().verify(token)
            val expiresAt = decodedJWT.expiresAt
            val now = Date()
            val timeToExpire = expiresAt.time - now.time
            
            // 如果剩余时间小于刷新阈值，刷新token
            if (timeToExpire < jwtConfig.refreshThreshold) {
                // 创建新token
                val newToken = JWT.create()
                    .withAudience(phone)
                    .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.expiration))
                    .sign(Algorithm.HMAC256(jwtConfig.secret))
                
                // 更新Redis中的token
                redisTemplate.opsForValue().set(
                    "$TOKEN_PREFIX$phone", 
                    newToken, 
                    jwtConfig.expiration, 
                    TimeUnit.MILLISECONDS
                )
                
                return newToken
            }
        } catch (e: Exception) {
            // Token验证失败，可能是无效或已过期
            return null
        }
        
        return null
    }
    
    /**
     * 使token无效（用于登出等场景）
     */
    open fun invalidateToken(phone: String) {
        redisTemplate.delete("$TOKEN_PREFIX$phone")
    }
}
