package com.btm.back.repository

import com.btm.back.dto.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

/**
 * 用户数据访问层
 * 负责用户相关的数据库操作
 * @author Trae AI
 * @date 2023-06-01
 */
interface UserRepository : JpaRepository<User, Long> {
    /**
     * 根据手机号查找用户
     * @param phone 手机号
     * @return 用户对象，如果不存在则返回null
     */
    fun findByPhone(@Param("phone") phone: String): User?
    
    /**
     * 根据账号查找用户
     * @param account 账号
     * @return 用户对象，如果不存在则返回null
     */
    fun findByAccount(@Param("account") account: String): User?
    
    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    fun findById(@Param("id") id:Int):User?
    
    /**
     * 查找未被禁用的用户
     * @param isbanned 是否被禁用
     * @param pageable 分页信息
     * @return 用户列表
     */
    fun findByIsbannedFalse(isbanned:Boolean,pageable: Pageable):List<User>?
    
    /**
     * 查找ID不在指定列表中的用户
     * @param list ID列表
     * @param pageable 分页信息
     * @return 用户列表
     */
    fun findByIdNotIn(list:List<Int>,pageable: Pageable):List<User>?
    
    /**
     * 查找ID在指定列表中的用户
     * @param list ID列表
     * @param pageable 分页信息
     * @return 用户列表
     */
    fun findByIdIn(list:List<Int>,pageable: Pageable):List<User>?
}