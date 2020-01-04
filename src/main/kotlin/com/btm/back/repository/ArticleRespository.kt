package com.btm.back.repository

import com.btm.back.dto.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

/**
 * @author hero
 */
@Repository
@Component
interface ArticleRespository : JpaRepository<Article, Int>{
}
