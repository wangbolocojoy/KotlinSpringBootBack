package com.btm.back.utils

import com.alibaba.fastjson.JSON

abstract class abstractObjectToString {
    override fun toString(): String {
        return JSON.toJSONString(this)
    }
}
