package com.btm.back.utils

class BaseResult {
    var status //状态码
            : Int? = null
    var msg //消息
            : String? = null
    var data //数据对象
            : Any? = null
    private val yes = false

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

    constructor(yes: Boolean, data: Any?) {
        if (yes) {
            status = HttpCode.successcode
            msg = HttpCode.successmsg
            this.data = data
        } else {
            status = HttpCode.failcode
            msg = HttpCode.failmsg
            this.data = data
        }
    }



}
