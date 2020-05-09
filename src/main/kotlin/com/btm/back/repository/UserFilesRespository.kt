package com.btm.back.repository

import com.btm.back.dto.UserFiles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface UserFilesRespository : JpaRepository<UserFiles, Long>{
    fun findUserFilesByUserid(@Param("userid") userid: String):List<UserFiles>
}
