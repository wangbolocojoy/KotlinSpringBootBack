package com.btm.back.utils

import com.fasterxml.jackson.annotation.JsonTypeInfo
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
class BaseResult {
    var status //状态码
            : Int? = null
    var msg //消息
            : String? = null
    var data //数据对象
            : Any? = null

    constructor()
    constructor(code: Int?, massege: String?) {
        status = code
        msg = massege
    }


    constructor(code: Int?, massege: String?, result: Any?) {
        status = code
        msg = massege
        data = result
    }

    constructor(msg: String?) {
        this.msg = msg
    }

    companion object{
        fun SECUESS(massege: String?, data: Any?):BaseResult{
            return BaseResult(HttpCode.successcode,massege,data)
        }
        fun SECUESS( data: Any?):BaseResult{
            return BaseResult(HttpCode.successcode,HttpCode.successmsg,data)
        }
        fun SECUESS( messager: String):BaseResult{
            return BaseResult(HttpCode.successcode,messager,null)
        }
        fun SECUESS():BaseResult{
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




}
