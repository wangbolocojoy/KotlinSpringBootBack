package com.btm.back.utils

interface HttpCode {
    companion object {
        const val successcode = 200
        const val failcode = 303
        const val notdatacode = 302
        const val tokenfiled = 304
        const val successmsg = "success"
        const val tokenfiledmsg = "token验证失败"
        const val passwordfiledmsg = "密码错误"
        const val phonefiledmsg = "手机号验证失败"
        const val failmsg = "错误"
        const val notdatamsg = "暂无数据"
        const val nouser = "暂无数据"
    }
}
