package com.btm.back.repository

import com.btm.back.dto.Follow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface FollowRespository : JpaRepository<Follow, Int> {
    fun findByUserid(@Param("userid") userid: Int):List<Follow>
    fun findByFollowid(@Param("followid") followid: Int):List<Follow>
}
