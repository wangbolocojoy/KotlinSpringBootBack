package com.btm.back.repository

import com.btm.back.dto.PhotoInfo
import com.btm.back.dto.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PhotoInfoRespository  : JpaRepository<PhotoInfo, Long>, JpaSpecificationExecutor<PhotoInfo> {
    fun findByPhotoclassificationid(Photoclassificationid:Int,pageable: Pageable):List<PhotoInfo>?
}