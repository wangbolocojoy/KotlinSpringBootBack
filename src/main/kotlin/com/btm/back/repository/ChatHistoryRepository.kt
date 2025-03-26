package com.btm.back.repository

import com.btm.back.vo.ChatHistoryVO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * 聊天历史数据访问层
 * 负责聊天历史记录的数据库操作
 * @author Trae AI
 * @date 2023-06-01
 */
@Repository
interface ChatHistoryRepository : JpaRepository<ChatHistoryVO, String> {
    
    /**
     * 根据用户ID查询聊天历史，按创建时间降序排序
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 聊天历史列表
     */
    @Query(value = "SELECT * FROM chat_history WHERE user_id = ?1 ORDER BY create_time DESC LIMIT ?3 OFFSET ?2 * ?3", nativeQuery = true)
    fun findByUserIdOrderByCreateTimeDesc(userId: Int, page: Int, size: Int): List<ChatHistoryVO>
    
    /**
     * 统计用户的聊天历史数量
     * @param userId 用户ID
     * @return 聊天历史数量
     */
    @Query("SELECT COUNT(c) FROM ChatHistoryVO c WHERE c.userId = ?1")
    fun countByUserId(userId: Int): Long
    
    /**
     * 删除用户的所有聊天历史
     * @param userId 用户ID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatHistoryVO c WHERE c.userId = ?1")
    fun deleteByUserId(userId: Int)
}