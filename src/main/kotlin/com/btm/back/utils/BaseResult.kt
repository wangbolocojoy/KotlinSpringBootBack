package com.btm.back.utils

import com.aliyuncs.AcsResponse
import com.aliyuncs.transform.UnmarshallerContext
class BaseResult: AcsResponse {
    var status //状态码
            : Int? = null
    var msg //消息
            : String? = null
    var data //数据对象
            : Any? = null

    constructor()
    constructor(code: Int?, message: String?) {
        status = code
        msg = message
    }

    constructor(code: Int?, message: String?, result: Any?) {
        status = code
        msg = message
        data = result
    }

    constructor(msg: String?) {
        this.msg = msg
    }

    companion object{
        fun SUCCESS(massege: String?, data: Any?):BaseResult{
            return BaseResult(HttpCode.successcode,massege,data)
        }
        fun SUCCESS( data: Any?):BaseResult{
            return BaseResult(HttpCode.successcode,HttpCode.successmsg,data)
        }
        fun SUCCESS( messager: String):BaseResult{
            return BaseResult(HttpCode.successcode,messager,null)
        }
        fun SUCCESS():BaseResult{
            return BaseResult(HttpCode.successcode,HttpCode.successmsg,"操作成功")
        }


        open fun FAIL(massege: String?, data: Any?):BaseResult{
            return BaseResult(HttpCode.failcode,massege,data)
        }
        open fun FAIL( data: Any?):BaseResult{
            return BaseResult(HttpCode.failcode,HttpCode.failmsg,data)
        }
        open fun FAIL(massege: String?):BaseResult{
            return BaseResult(HttpCode.failcode,massege,null)
        }
        open fun FAIL():BaseResult{
            return BaseResult(HttpCode.failcode,HttpCode.failmsg,"操作失败")
        }

    }

    override fun getInstance(p0: UnmarshallerContext?): AcsResponse {
       return BaseResult.SUCCESS(p0?.httpResponse)
    }


}
