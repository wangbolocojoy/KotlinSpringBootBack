package com.btm.back.repository

import com.btm.back.dto.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PostRespository: JpaRepository<Post,Long>, JpaSpecificationExecutor<Post> {
    fun findByUserIdOrderByCreatTimeDesc(userId:Int,pageable: Pageable): Page<Post>?
    fun findByOrderByCreatTimeDesc(pageable: Pageable):List<Post>?
    fun findByPostPublicAndUserIdOrderByCreatTimeDesc(postPublic:Boolean,userId:Int,pageable: Pageable): Page<Post>?
    fun findByPostPublicOrderByCreatTimeDesc(postPublic:Boolean,pageable: Pageable):List<Post>?
    fun findByPostPublicAndPostStateOrderByCreatTimeDesc(postPublic:Boolean,postState:Int,pageable: Pageable):List<Post>?
    fun findByPostReportGreaterThanOrderByCreatTimeDesc(postReport:Int,pageable: Pageable):List<Post>?

    fun findByUserIdAndPostPublicAndPostStateOrderByCreatTimeDesc(userId:Int,postPublic:Boolean,postState:Int,pageable: Pageable):List<Post>?
    fun findByPostPublicFalseOrderByCreatTimeDesc(postPublic:Boolean,pageable: Pageable):List<Post>?
    fun deleteById(id:Int)
    fun findById(id:Int):Post?

}
