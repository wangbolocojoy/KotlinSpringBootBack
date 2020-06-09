package com.btm.back.service

import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult

interface FanceService {
    /**
     * 获取粉丝列表
     */
    fun getFanceList(body: ReqBody): BaseResult
}
