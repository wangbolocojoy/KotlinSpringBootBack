package com.btm.back.utils

import com.aliyuncs.AcsResponse
import com.aliyuncs.transform.UnmarshallerContext

class StsResult: AcsResponse {
    var StatusCode //状态码
            : Int? = null
    var AccessKeyId //消息
            : String? = null

     var AccessKeySecret //消息
            : String? = null

     var Expiration //消息
            : String? = null

     var SecurityToken //消息
            : String? = null

    var msg //消息
            : String? = null
    var data //数据对象
            : Any? = null

    constructor()
    constructor(code: Int?, massege: String?) {
        StatusCode = code
        msg = massege
    }


    constructor(code: Int?, massege: String?, result: Any?) {
        StatusCode = code
        msg = massege
        data = result
    }

    constructor(msg: String?) {
        this.msg = msg
    }

    companion object{
        fun SECUESS(massege: String?, data: Any?):StsResult{
            return StsResult(HttpCode.successcode,massege,data)
        }
        fun SECUESS( data: Any?):StsResult{
            return StsResult(HttpCode.successcode,HttpCode.successmsg,data)
        }
        fun SECUESS( messager: String):StsResult{
            return StsResult(HttpCode.successcode,messager,null)
        }
        fun SECUESS():StsResult{
            return StsResult(HttpCode.successcode,HttpCode.successmsg,"操作成功")
        }


        open fun FAIL(massege: String?, data: Any?):StsResult{
            return StsResult(HttpCode.failcode,massege,data)
        }
        open fun FAIL( data: Any?):StsResult{
            return StsResult(HttpCode.failcode,HttpCode.failmsg,data)
        }
        open fun FAIL(massege: String?):StsResult{
            return StsResult(HttpCode.failcode,massege,null)
        }
        open fun FAIL():StsResult{
            return StsResult(HttpCode.failcode,HttpCode.failmsg,"操作失败")
        }

    }

    override fun getInstance(p0: UnmarshallerContext?): AcsResponse {
        return StsResult.SECUESS(p0?.httpResponse)
    }


}
