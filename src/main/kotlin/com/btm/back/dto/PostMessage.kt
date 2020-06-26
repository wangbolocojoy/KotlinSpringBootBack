package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "PostMessage")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class PostMessage  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var postId:Int? = null
    var postMsgId:Int? = null
    var postPostMessage: String? = null
    var postMsgCreatTime: Date? = null
    var messageStart:Int? = 0


}
