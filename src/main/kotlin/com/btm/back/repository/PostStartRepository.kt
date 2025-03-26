package com.btm.back.repository

import com.btm.back.dto.PostStart
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostStartRepository : JpaRepository<PostStart,Long>{
    fun findByPostId(postId:Int):List<PostStart>?
    fun findByPostId(postId:Int,pageable: Pageable):List<PostStart>?
    fun findByUserId(userId:Int):List<PostStart>?
}
