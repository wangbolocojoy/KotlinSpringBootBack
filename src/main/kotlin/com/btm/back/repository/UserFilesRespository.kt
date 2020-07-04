package com.btm.back.repository

import com.btm.back.dto.UserFiles
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface UserFilesRespository : JpaRepository<UserFiles, Long>{
    fun findAllByPostId(@Param("postId") postId: Int):List<UserFiles>?
    fun findByUserIdOrderByIdDesc(userId:Int):List<UserFiles>?
    fun findByUserIdOrderByIdDesc(userId:Int,pageable: Pageable):List<UserFiles>?
}
