package com.btm.back.service

import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult

interface FollowService {
    /**
     * 获取关注列表
     */
    fun getFollowList(body: ReqBody): BaseResult

    /**
     * 获取粉丝列表
     */
    fun getFanceList(body: ReqBody): BaseResult
    /**
     * 关注用户
     */
    fun followUser(body: ReqBody): BaseResult
    /**
     * 取消关注
     */
    fun unFollowUser(body: ReqBody): BaseResult

    /**
     * 获取推荐人列表
     */
    fun getRecommend(body: ReqBody): BaseResult
}
