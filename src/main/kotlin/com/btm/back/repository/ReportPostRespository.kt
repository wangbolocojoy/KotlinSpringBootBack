package com.btm.back.repository

import com.btm.back.dto.ReportPost
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ReportPostRespository : JpaRepository<ReportPost, Long>, JpaSpecificationExecutor<ReportPost> {
    fun findByOrderByReportDateTimeDesc(pageable: Pageable):List<ReportPost>?
    fun findByPostId(postId:Int):List<ReportPost>?
}
