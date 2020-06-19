package com.btm.back.service

import com.btm.back.bean.PageBody
import com.btm.back.bean.PostBody
import com.btm.back.utils.BaseResult

interface PostService {
    fun sendPost(body: PostBody): BaseResult
    fun getPostByUserId(body: PageBody): BaseResult
    fun getPosts(body: PageBody): BaseResult
    fun deletePost(body: PageBody):BaseResult
    fun getPostDetail(body: PageBody):BaseResult
}