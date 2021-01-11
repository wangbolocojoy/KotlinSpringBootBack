package com.btm.back

import com.btm.back.utils.AesUtil


object test1 {

    @JvmStatic
    fun main(args: Array<String>) { //
        //3dedf653961aaa0bb7bf4508664bb0cd
        val content = "离开水电费水电费路上的风景"
        val key = AesUtil.generateKey()
        //73248c2262eb9bcc1212b5cdac7472f8f00e13b124a0d25371f98d2d392dd70044ad72e3da58f3bb3e8bd2a17cd46d8a
        val jiami = AesUtil.encode(key,content)
        val jiemi = AesUtil.decode(key,jiami)
        //
    }

    fun checktextcontent(){

    }
}
