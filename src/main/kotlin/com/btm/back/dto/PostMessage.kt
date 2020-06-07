package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "PostMessage")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class PostMessage  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = 0
    var userid: Int? = 0
    var postTitle: String? = ""
    var postDetail: String? = ""
    var postCreatTime: String? = ""
    var postImageId: Int? = 0
    var postaddress: String? = ""
    var postLikeStarts: Int? = 0


}
