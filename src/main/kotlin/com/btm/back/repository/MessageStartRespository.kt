package com.btm.back.repository

import com.btm.back.dto.MessageStart
import com.btm.back.dto.Post
import org.springframework.data.jpa.repository.JpaRepository

interface MessageStartRespository: JpaRepository<MessageStart, Long> {
    fun findByMsgId(msgId:Int):List<MessageStart>?
    fun findByUserId(userId:Int):List<MessageStart>?
    fun findById(id:Int): MessageStart?
}
