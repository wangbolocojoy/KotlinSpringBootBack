package com.btm.back.dto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
@Entity
@Table(name = "UserFiles")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class UserFiles{
    @Id
    @GeneratedValue
    var id: Int? = 0
    var userid: String? = ""
    var filetype: String? = ""
    var filename: String? = ""
    var fileurl:String?=""
    var fileGroupId:Int?=0
    var fileLikes:Int =0
    var fileseenum:Int =0
    var fileispublic:Boolean =false
    override fun toString(): String {
        return "UserFiles(id=$id, userid=$userid, filetype=$filetype, filename=$filename, fileurl=$fileurl, fileGroupId=$fileGroupId, fileLikes=$fileLikes, fileseenum=$fileseenum, fileispublic=$fileispublic)"
    }


}
