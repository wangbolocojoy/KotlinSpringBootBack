package com.btm.back.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.btm.back.dto.User
import org.springframework.stereotype.Service

@Service("TokenService")
object TokenService {
    fun getToken(user: User): String {
        var token = ""
        token = JWT.create().withAudience(user.phone) // 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(user.password)) // 以 password 作为 token 的密钥;
        return token
    }
}
