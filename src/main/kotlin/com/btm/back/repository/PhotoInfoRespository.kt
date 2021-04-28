package com.btm.back.repository

import com.btm.back.dto.PhotoInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PhotoInfoRespository  : JpaRepository<PhotoInfo, Long>, JpaSpecificationExecutor<PhotoInfo> {

}