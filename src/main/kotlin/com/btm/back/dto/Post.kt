package com.btm.back.dto


import com.btm.back.bean.PageBody
import com.btm.back.utils.BaseResult
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "Post")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
//@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
class Post  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var postDetail:String? = null
    var postAddress:String? = null
    var postPublic:Boolean? = false
    var postStarts:Int? = 0
    var creatTime:Date? = null
    var latitude:String? = null
    var longitude:String? = null
    var postMessageNum:Int? = 0
    var postState:Int? = 0
    var postReport:Int? = 0
}
