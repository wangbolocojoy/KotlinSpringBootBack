package com.btm.back.mapper

import com.btm.back.vo.ChatHistoryVO
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * 聊天历史数据访问层
 * 负责聊天历史记录的数据库操作
 * @author Trae AI
 * @date 2023-06-01
 */
@Mapper
interface ChatHistoryMapper {
    
    /**
     * 根据用户ID查询聊天历史，按创建时间降序排序
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 聊天历史列表
     */
    fun findByUserIdOrderByCreateTimeDesc(
        @Param("userId") userId: Int, 
        @Param("page") page: Int, 
        @Param("size") size: Int
    ): List<ChatHistoryVO>
    
    /**
     * 统计用户的聊天历史数量
     * @param userId 用户ID
     * @return 聊天历史数量
     */
    fun countByUserId(@Param("userId") userId: Int): Long
    
    /**
     * 删除用户的所有聊天历史
     * @param userId 用户ID
     * @return 影响的行数
     */
    fun deleteByUserId(@Param("userId") userId: Int): Int
    
    /**
     * 保存聊天历史
     * @param chatHistory 聊天历史对象
     * @return 影响的行数
     */
    fun insert(chatHistory: ChatHistoryVO): Int
}