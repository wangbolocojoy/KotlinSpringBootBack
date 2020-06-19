package com.btm.back.repository

import com.btm.back.dto.PostMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface PostMessageRespository :JpaRepository<PostMessage,Long>{
    fun findAllByPostId(@Param("postId") postId: Int, pageable: Pageable) : Page<PostMessage>
}
