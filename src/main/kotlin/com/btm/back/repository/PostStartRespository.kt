package com.btm.back.repository

import com.btm.back.dto.PostStart
import org.springframework.data.jpa.repository.JpaRepository

interface PostStartRespository : JpaRepository<PostStart,Long>{
    fun findByPostId(postId:Int):List<PostStart>?
    fun findByUserId(userId:Int):List<PostStart>?
}
