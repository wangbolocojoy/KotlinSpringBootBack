package com.btm.back.repository

import com.btm.back.dto.Authentication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface AuthenticationRespository :JpaRepository<Authentication,Long>{
    fun findById(@Param("userId") userId:Int): Authentication?
}
