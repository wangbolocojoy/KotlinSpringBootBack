package com.btm.back.service

import com.aliyuncs.AcsResponse
import com.btm.back.bean.PageBody
import com.btm.back.bean.PostBody
import com.btm.back.bean.RestPostBody
import com.btm.back.utils.BaseResult
import com.btm.back.utils.StsResult
import org.springframework.web.multipart.MultipartFile

interface PostService {
    fun sendPost(body: PostBody, uploadFile: ArrayList<MultipartFile>): BaseResult
    fun getPostByUserId(body: PageBody): BaseResult
    fun getPosts(body: PageBody): BaseResult
    fun deletePost(body: PageBody):BaseResult
    fun updatePostLikeStartt(body: PageBody):BaseResult
    fun isHaveNewPost(body: PageBody):BaseResult
    fun getMyPosts(body: PageBody):BaseResult
    fun updatePosts(body: PageBody):BaseResult
    fun reportPostByPostId(body: RestPostBody):BaseResult
    fun getReportList(body: PageBody):BaseResult
    fun getExamineList(body: PageBody):BaseResult
    fun gettoken(): BaseResult
}
