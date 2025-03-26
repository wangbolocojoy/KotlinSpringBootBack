package com.btm.back.repository

import com.btm.back.dto.Article
import com.btm.back.dto.Photography
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PhotographyRepository : JpaRepository<Photography, Int>, JpaSpecificationExecutor<Photography> {
    /**
     * 根据作者查找文章
     * @param author 作者名称
     * @param pageable 分页参数
     * @return 文章分页列表
     */
    fun findByAuthor(author: String, pageable: Pageable): Page<Photography>
}