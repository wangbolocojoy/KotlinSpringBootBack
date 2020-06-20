package com.btm.back.dto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "UserFiles")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class UserFiles{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var userid: Int? = null
    var postid:Int? = null
    var filetype: String? = null
    var originalFilename: String? = null
    var fileurl:String?= null
    var fileLikes:Int? = null
    var fileseenum:Int? = null


}
