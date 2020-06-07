package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

/**
 * @author hero
 */
@Entity
@Table(name = "ArticleContent")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class ArticleContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var articleName: String? = null
    var articleImage: String? = null
    var articleTitle: String? = null
    var articleAuthor: String? = null
    var articleDetail: String? = null
    var articleLikes: Int? = null

}
