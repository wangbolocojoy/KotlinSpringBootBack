package com.btm.back.dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "Post")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Post  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var posttitle:String? = null
    var postdetail:String? = null
    var postaddress:String? = null
    var postpublic:Boolean? = false
    var poststarts:Int? = 0

}
