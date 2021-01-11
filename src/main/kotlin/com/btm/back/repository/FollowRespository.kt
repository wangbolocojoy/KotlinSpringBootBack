package com.btm.back.repository

import com.btm.back.dto.Follow
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface FollowRespository : JpaRepository<Follow, Int> {
    fun findByUserId(@Param("userId") userId: Int,pageable: Pageable):List<Follow>?
    fun findAllByUserId(@Param("userId") userId: Int):List<Follow>?
    fun findByUserIdAndFollowId( userId: Int,followId: Int):Follow?
    fun findByFollowId(@Param("followId") followId: Int,pageable: Pageable):List<Follow>?
    fun findAllByFollowId(@Param("followId") followId: Int):List<Follow>?
    fun findByFollowIdAndUserIdNotIn(followId: Int,list:List<Int>,pageable: Pageable):List<Follow>?
    fun findByUserIdAndFollowIdNotIn(userId: Int,list:List<Int>,pageable: Pageable) :List<Follow>?
}
