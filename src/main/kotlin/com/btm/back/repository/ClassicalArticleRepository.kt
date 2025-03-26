package com.btm.back.repository

import com.btm.back.dto.ClassicalArticle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 国学篇章数据库操作接口
 * @author Trae AI
 * @date 2023-06-01
 */
@Repository
interface ClassicalArticleRepository : JpaRepository<ClassicalArticle, Int> {
    /**
     * 根据作者查询国学篇章
     * @param author 作者名称
     * @param pageable 分页参数
     * @return 国学篇章列表
     */
    fun findByAuthor(author: String, pageable: Pageable): Page<ClassicalArticle>
    
    /**
     * 根据朝代查询国学篇章
     * @param dynasty 朝代
     * @param pageable 分页参数
     * @return 国学篇章列表
     */
    fun findByDynasty(dynasty: String, pageable: Pageable): Page<ClassicalArticle>
}