package com.btm.back.repository

import com.btm.back.dto.Fance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface FanceRespository :JpaRepository<Fance,Int>{
    fun findByUserid(@Param("userid") userid: Int):List<Fance>
}
