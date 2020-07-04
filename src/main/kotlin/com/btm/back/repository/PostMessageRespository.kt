package com.btm.back.repository

import com.btm.back.dto.PostMessage
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostMessageRespository : JpaRepository<PostMessage,Int>{
    fun findByPostId(postId:Int,pageable: Pageable):List<PostMessage>?
    fun findByPostId(postId:Int):List<PostMessage>?
    fun findByPostIdOrderByPostMsgCreatTimeDesc(postId:Int,pageable: Pageable):List<PostMessage>?
    fun findByPostIdOrderByPostMsgCreatTimeDesc(postId:Int):List<PostMessage>?
    fun findByUserIdOrderByPostMsgCreatTimeDesc(userId:Int):List<PostMessage>?
    fun findByUserIdOrderByPostMsgCreatTimeDesc(userId:Int,pageable: Pageable):List<PostMessage>?
}
