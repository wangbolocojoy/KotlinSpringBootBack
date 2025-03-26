package com.btm.back.repository

import com.btm.back.dto.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

/**
 * 文章数据访问接口
 * @author Trae AI
 * @date 2023-06-01
 */
interface ArticleRepository : JpaRepository<Article, Int>, JpaSpecificationExecutor<Article> {
    /**
     * 根据作者查找文章
     * @param author 作者名称
     * @param pageable 分页参数
     * @return 文章分页列表
     */
    fun findByAuthor(author: String, pageable: Pageable): Page<Article>
}