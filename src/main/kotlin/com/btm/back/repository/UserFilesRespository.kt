package com.btm.back.repository

import com.btm.back.dto.UserFiles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface UserFilesRespository : JpaRepository<UserFiles, Long>{
    fun findAllByPostid(@Param("postid") postid: Int):List<UserFiles>

}
