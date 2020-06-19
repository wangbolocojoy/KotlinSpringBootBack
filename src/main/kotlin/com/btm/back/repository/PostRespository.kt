package com.btm.back.repository

import com.btm.back.dto.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRespository: JpaRepository<Post,Long>{
    fun findAllByUserId(userId:Int,pageable: Pageable): Page<Post>
    fun deleteById(id:Int)
    fun findById(id:Int):Post

}
