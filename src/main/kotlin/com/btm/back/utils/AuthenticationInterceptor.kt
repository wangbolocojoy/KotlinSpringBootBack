package com.btm.back.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.btm.back.config.JwtConfig
import com.btm.back.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.*
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.Instant

class AuthenticationInterceptor : HandlerInterceptor {
    @Autowired
    var userService: UserService? = null
    
    @Autowired
    private lateinit var jwtConfig: JwtConfig

    @Autowired
    private lateinit var tokenService: TokenService
    
    private val logger = LoggerFactory.getLogger(AuthenticationInterceptor::class.java)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Throws(Exception::class)
    override fun preHandle(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, `object`: Any): Boolean { // 从 http 请求头中取出 token
        val token = httpServletRequest.getHeader("token")
        // 如果不是映射到方法直接通过
        if (`object` !is HandlerMethod) {
            return true
        }
        val method = `object`.method
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken::class.java)) {
            val passToken = method.getAnnotation(PassToken::class.java)
            if (passToken.required) {
                return true
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken::class.java)) {
            val userLoginToken = method.getAnnotation(UserLoginToken::class.java)
            if (userLoginToken.required) { // 执行认证
                if (token == null) {
                    throw KtErrorException(ErrorCodeEnum.TOKENISNULL)
                }
                // 获取 token 中的 user id
                val phone: String
                phone = try {
                    JWT.decode(token).audience[0]
                } catch (j: JWTDecodeException) {
                    throw KtErrorException(ErrorCodeEnum.TOKENJSONERROR)
                }
                val user = userService!!.findUserById(phone) ?: throw  KtErrorException(ErrorCodeEnum.USERISNOTFOUND)
                if (user.isbanned == true){
                    throw KtErrorException(ErrorCodeEnum.USERISBANDE)
                }
                
                try {
                    // 使用JWT验证token基本结构
                    JWT.require(Algorithm.HMAC256(jwtConfig.secret)).build().verify(token)
                    
                    // 检查token是否需要刷新（使用Redis优化的方法）
                    val newToken = tokenService.checkAndRefreshToken(phone, token)
                    
                    // 如果需要刷新token
                    if (newToken != null) {
                        // 更新HTTP响应头
                        httpServletResponse.setHeader("token", newToken)
                        httpServletResponse.setHeader("Access-Control-Expose-Headers", "token")
                        
                        // 更新用户token
                        user.token = newToken
                        userService?.let { it.updateUserToken(user) }
                        
                        // 解析新token，记录日志
                        val newDecodedJWT = JWT.decode(newToken)
                        val newExpiresAt = newDecodedJWT.expiresAt
                        val newExpiresDateTime = Instant.ofEpochMilli(newExpiresAt.time)
                            .atZone(ZoneId.systemDefault())
                            .format(dateFormatter)
                        
                        logger.info("Token已续期(Redis) - 用户: ${user.phone}, 新过期时间: $newExpiresDateTime")
                    } else {
                        // 记录未刷新token的信息
                        val decodedJWT = JWT.decode(token)
                        val expiresAt = decodedJWT.expiresAt
                        val expiresDateTime = Instant.ofEpochMilli(expiresAt.time)
                            .atZone(ZoneId.systemDefault())
                            .format(dateFormatter)
                        val daysToExpire = (expiresAt.time - System.currentTimeMillis()) / (1000 * 60 * 60 * 24.0)
                        
                        logger.debug("Token有效(Redis) - 用户: ${user.phone}, 过期时间: $expiresDateTime, 剩余: ${String.format("%.2f", daysToExpire)} 天")
                    }
                } catch (e: JWTVerificationException) {
                    logger.error("Token验证失败: ${e.message}")
                    throw KtErrorException(ErrorCodeEnum.TOKENERROR)
                }
                return true
            }
        }
        return true
    }

    @Throws(Exception::class)
    override fun postHandle(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, o: Any, modelAndView: ModelAndView?) {
    }

    @Throws(Exception::class)
    override fun afterCompletion(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, o: Any, e: Exception?) {
    }
}
