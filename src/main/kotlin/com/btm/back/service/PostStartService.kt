package com.btm.back.service

import com.btm.back.bean.PostBody
import com.btm.back.utils.BaseResult

interface PostStartService {
    fun start(body: PostBody): BaseResult
    fun unStart(body: PostBody): BaseResult
    fun getPostStartList(body: PostBody): BaseResult
    fun getUserStartList(body: PostBody): BaseResult
}
