package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "Article")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Article {
    @Id
    @GeneratedValue
    var id: Int? = null
    var article_Id: Int? = null
    var article_Title: String? = null
    var article_Creattime: Long? = null
    var article_Updatetime: Long? = null
    var article_Author: String? = null
    var article_AuthorId: Int? = null
    var article_Type: Int? = null
    var article_Address_Id: Int? = null
    var article_Typename: String? = null
    var article_Carry_Number: Int? = null
    var article_Relase_Name: String? = null
    var article_State: String? = null
    override fun toString(): String {
        return "Article(id=$id, article_Id=$article_Id, article_Title=$article_Title, article_Creattime=$article_Creattime, article_Updatetime=$article_Updatetime, article_Author=$article_Author, article_AuthorId=$article_AuthorId, article_Type=$article_Type, article_Address_Id=$article_Address_Id, article_Typename=$article_Typename, article_Carry_Number=$article_Carry_Number, article_Relase_Name=$article_Relase_Name, article_State=$article_State)"
    }

}
