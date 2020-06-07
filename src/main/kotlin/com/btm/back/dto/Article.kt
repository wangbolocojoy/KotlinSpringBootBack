package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "Article")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    var article_Address:String? = null



}
