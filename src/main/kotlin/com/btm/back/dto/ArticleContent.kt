package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * @author hero
 */
@Entity
@Table(name = "ArticleContent")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class ArticleContent {
    @Id
    @GeneratedValue
    var id: Int? = null
    var articleName: String? = null
    var articleImage: String? = null
    var articleTitle: String? = null
    var articleAuthor: String? = null
    var articleDetail: String? = null
    var articleLikes: Int? = null
    override fun toString(): String {
        return "ArticleContent(id=$id, articleName=$articleName, articleImage=$articleImage, articleTitle=$articleTitle, articleAuthor=$articleAuthor, articleDetail=$articleDetail, articleLikes=$articleLikes)"
    }

}
