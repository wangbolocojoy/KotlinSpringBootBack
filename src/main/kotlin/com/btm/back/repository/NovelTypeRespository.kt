package com.btm.back.repository

import com.btm.back.dto.NovelType
import org.springframework.data.jpa.repository.JpaRepository

interface NovelTypeRespository : JpaRepository<NovelType, Long?>
