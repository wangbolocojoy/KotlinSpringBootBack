package com.btm.back.repository

import com.btm.back.dto.Developer
import org.springframework.data.jpa.repository.JpaRepository

interface DeveloperRespository: JpaRepository<Developer, Long> {
     fun findById(id:Int):Developer?
}
