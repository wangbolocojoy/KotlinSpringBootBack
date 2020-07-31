package com.btm.back.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.btm.back.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationInterceptor : HandlerInterceptor {
    @Autowired
    var userService: UserService? = null

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
                val userId: String
                userId = try {
                    JWT.decode(token).audience[0]
                } catch (j: JWTDecodeException) {
                    throw KtErrorException(ErrorCodeEnum.TOKENJSONERROR)
                }
                val user = userService!!.findUserById(userId) ?: throw  KtErrorException(ErrorCodeEnum.USERISNOTFOUND)
                if (user.isbanned == true){
                    throw KtErrorException(ErrorCodeEnum.USERISBANDE)
                }
                // 验证 token
                val jwtVerifier = JWT.require(Algorithm.HMAC256(user.password)).build()
                try {
                    jwtVerifier.verify(token)
                } catch (e: JWTVerificationException) {
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
