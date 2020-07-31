package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "PostMessage")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
//@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
class PostMessage  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var postId:Int? = null
    var postMsgId:Int? = null
    var message: String? = null
    var postMsgCreatTime: Date? = null
    var messageStart:Int? = null
    var replyUserId:Int? = null
    var replyNickName:String? = null


}
