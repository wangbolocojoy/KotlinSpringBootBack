package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "PostMessage")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class PostMessage  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var postTitle: String? = null
    var postDetail: String? = null
    var postCreatTime: String? = null
    var postImageId: Int? = null
    var postAddress: String? = null
    var postLikeStarts: Int? = null
    var postLevaingMessage:Int? = null

}
