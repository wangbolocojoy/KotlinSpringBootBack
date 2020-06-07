package com.btm.back.dto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "UserFiles")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class UserFiles{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = 0
    var userid: String? = ""
    var filetype: String? = ""
    var filename: String? = ""
    var fileurl:String?=""
    var fileGroupId:Int?=0
    var fileLikes:Int =0
    var fileseenum:Int =0
    var fileispublic:Boolean =false


}
