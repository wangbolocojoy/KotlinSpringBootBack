package com.btm.back.vo

import com.fasterxml.jackson.annotation.JsonTypeInfo

class UserFilesVO {
    var id: Int? = null
    var userId: String? = null
    var fileUrl:String?= null
    var fileType: String? = null
    override fun toString(): String {
        return "UserFilesVO(id=$id, userId=$userId, fileUrl=$fileUrl, fileType=$fileType)"
    }


}
