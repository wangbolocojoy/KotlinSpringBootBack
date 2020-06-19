package com.btm.back.repository

import com.btm.back.dto.Fance
import org.springframework.data.jpa.repository.JpaRepository

interface FanceRespository :JpaRepository<Fance,Int>{
    fun findAllByUserId( userId: Int):List<Fance>
}
