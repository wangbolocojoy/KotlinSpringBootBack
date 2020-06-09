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
    var userid: String? = null
    var filetype: String? = null
    var filename: String? = null
    var fileurl:String?= null
    var fileGroupId:Int?= null
    var fileLikes:Int? = null
    var fileseenum:Int? = null
    var fileispublic:Boolean? =false


}
