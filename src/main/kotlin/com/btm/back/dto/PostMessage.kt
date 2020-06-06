package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "PostMessage")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class PostMessage  {
    @Id
    @GeneratedValue
    var id: Int? = 0
    var userid: Int? = 0
    var postTitle: String? = ""
    var postDetail: String? = ""
    var postCreatTime: String? = ""
    var postImageId: Int? = 0
    var postaddress: String? = ""
    var postLikeStarts: Int? = 0


}
