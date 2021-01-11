package com.btm.back.repository

import com.btm.back.dto.PostMessage
import com.btm.back.dto.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface PostMessageRespository : JpaRepository<PostMessage,Long>{
    fun findByPostId(postId:Int,pageable: Pageable):List<PostMessage>?
    fun findByPostId(postId:Int):List<PostMessage>?
    fun findById( id:Int): PostMessage?
    fun findByPostIdOrderByPostMsgCreatTimeDesc(postId:Int,pageable: Pageable):List<PostMessage>?
    fun findByPostMsgIdOrderByPostMsgCreatTimeDesc(postId:Int,pageable: Pageable):List<PostMessage>?
    fun findByPostIdOrderByPostMsgCreatTimeDesc(postId:Int):List<PostMessage>?
    fun findByPostMsgIdOrderByPostMsgCreatTimeDesc(postId:Int):List<PostMessage>?
    fun findByPostMsgIdOrderByPostMsgCreatTimeAsc(postId:Int):List<PostMessage>?
    fun findByUserIdOrderByPostMsgCreatTimeDesc(userId:Int):List<PostMessage>?
    fun findByUserIdOrderByPostMsgCreatTimeDesc(userId:Int,pageable: Pageable):List<PostMessage>?
}
