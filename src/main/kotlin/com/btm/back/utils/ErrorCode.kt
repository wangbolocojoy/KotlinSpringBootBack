package com.btm.back.utils

interface ErrorCode {


    /**
     * 获取错误码
     * @return
     */
    fun getCode(): String?

    /**
     * 获取错误信息
     * @return
     */
    fun getDescription(): String?
}
