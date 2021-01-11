package com.btm.back.repository

import com.btm.back.dto.Favorites
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface FavoritesRespository : JpaRepository<Favorites,Int>{
    fun findByUserId(userId:Int,pageable: Pageable):List<Favorites>?
    fun findByUserId(userId:Int):List<Favorites>?
    fun findByPostId(postId:Int):List<Favorites>?
    fun findByPostId(postId:Int,pageable: Pageable):List<Favorites>?
}
