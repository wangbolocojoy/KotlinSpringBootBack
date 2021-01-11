package com.btm.back.repository

import com.btm.back.dto.BackInfoPlist
import org.springframework.data.jpa.repository.JpaRepository

interface BackInfoPlistRespository: JpaRepository<BackInfoPlist, Long> {
    fun findByUserIdIn(list:List<Int>):List<BackInfoPlist>?
    fun findByUserId(userId: Int):List<BackInfoPlist>?
    fun findByUserIdAndBackId(userId: Int,backId:Int):List<BackInfoPlist>?
}
