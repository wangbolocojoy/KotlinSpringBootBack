package com.btm.back.dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "Post")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Post  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var postTitle:String? = null
    var postDetail:String? = null
    var postAddress:String? = null
    var postPublic:Boolean? = false
    var postStarts:Int? = 0
    var postCreatTime:Date? = null
}
