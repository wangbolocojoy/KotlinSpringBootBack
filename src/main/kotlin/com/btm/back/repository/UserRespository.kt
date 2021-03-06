package com.btm.back.repository

import com.btm.back.dto.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

/**
 * @author hero
 */

interface UserRespository : JpaRepository<User, Long> {
    fun findByPhone(@Param("phone") phone: String): User?
    fun findByAccount(@Param("account") account: String): User?
    fun findById(@Param("id") id:Int):User?
    fun findByIsbannedFalse(isbanned:Boolean,pageable: Pageable):List<User>?
    fun findByIdNotIn(list:List<Int>,pageable: Pageable):List<User>?
    fun findByIdIn(list:List<Int>,pageable: Pageable):List<User>?
}
