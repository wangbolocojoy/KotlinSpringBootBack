package com.btm.back.mapper

import com.btm.back.dto.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.springframework.data.domain.Pageable

/**
 * 用户数据访问层
 * 负责用户相关的数据库操作
 * @author Trae AI
 * @date 2023-06-01
 */
@Mapper
interface UserMapper {
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
    fun findById(@Param("id") id: Int): User?
    
    /**
     * 查找未被禁用的用户
     * @param isbanned 是否被禁用
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 用户列表
     */
    fun findByIsbannedFalse(@Param("isbanned") isbanned: Boolean, @Param("offset") offset: Int, @Param("limit") limit: Int): List<User>?
    
    /**
     * 查找ID不在指定列表中的用户
     * @param list ID列表
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 用户列表
     */
    fun findByIdNotIn(@Param("list") list: List<Int>, @Param("offset") offset: Int, @Param("limit") limit: Int): List<User>?
    
    /**
     * 保存用户
     * @param user 用户对象
     * @return 影响的行数
     */
    fun save(user: User): Int
    
    /**
     * 更新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    fun update(user: User): Int
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 影响的行数
     */
    fun deleteById(@Param("id") id: Int): Int
    
    /**
     * 查找所有用户
     * @return 用户列表
     */
    fun findAll(): List<User>?
}