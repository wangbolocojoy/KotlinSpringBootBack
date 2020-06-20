package com.btm.back.repository

import com.btm.back.dto.Follow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface FollowRespository : JpaRepository<Follow, Int> {
    fun findByUserId(@Param("userId") userId: Int):List<Follow>
    fun findByFollowId(@Param("followId") followId: Int):List<Follow>
}
