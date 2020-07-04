package com.btm.back.dto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import javax.persistence.*


@Entity
@Table(name = "UserFiles")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
class UserFiles{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userId: Int? = null
    var postId:Int? = null
    var fileType: String? = null
    var originalFileName: String? = null
    var fileUrl:String?= null
    var fileLikes:Int? = null


}
