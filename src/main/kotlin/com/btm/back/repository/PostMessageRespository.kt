package com.btm.back.repository

import com.btm.back.dto.PostMessage
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostMessageRespository : JpaRepository<PostMessage,Int>{
    fun findByPostId(postId:Int,pageable: Pageable):List<PostMessage>?
    fun findByPostId(postId:Int):List<PostMessage>?
}
