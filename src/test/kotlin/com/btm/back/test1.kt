package com.btm.back

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlin.random.Random

object test1 {
    @JvmStatic
    fun main(args: Array<String>) { //
              val code = Random.nextInt(999999).toString()
        System.out.print(getToken())
    }

    fun getToken(): String
    {
        var token = ""
        token = JWT.create().withAudience("15390190780") // 将 user phone 保存到 token 里面
                .sign(Algorithm.HMAC256("f17e148a4086567563ae390e7f1e7162")) // 以 password 作为 token 的密钥;
        return token
    }
}
