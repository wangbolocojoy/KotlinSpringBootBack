package com.btm.back.service

import com.btm.back.bean.MessageBody
import com.btm.back.bean.PageBody
import com.btm.back.utils.BaseResult

interface PostMessageService {
    fun getPostMessagesByPostId(body: PageBody): BaseResult
    fun sendMessage(body: MessageBody): BaseResult
    fun deleteMessage(body: MessageBody): BaseResult
}
