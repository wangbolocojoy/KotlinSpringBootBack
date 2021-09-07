package com.btm.back.repository

import com.btm.back.dto.PhotoInfo
import com.btm.back.dto.PhotoType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PhotoTypeRespository : JpaRepository<PhotoType, Long>, JpaSpecificationExecutor<PhotoType> {
}