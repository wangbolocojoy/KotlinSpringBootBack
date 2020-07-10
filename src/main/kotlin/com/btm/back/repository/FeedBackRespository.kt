package com.btm.back.repository

import com.btm.back.dto.FeedBack
import org.springframework.data.jpa.repository.JpaRepository

interface FeedBackRespository : JpaRepository<FeedBack, Long>{
}
