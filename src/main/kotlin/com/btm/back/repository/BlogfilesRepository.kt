package com.btm.back.repository

import com.btm.back.dto.Article
import org.springframework.data.jpa.repository.JpaRepository
import com.btm.back.dto.Blogfiles
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface BlogfilesRepository : JpaRepository<Blogfiles, Int>, JpaSpecificationExecutor<Blogfiles> {
    // 可在此处添加自定义查询方法
}