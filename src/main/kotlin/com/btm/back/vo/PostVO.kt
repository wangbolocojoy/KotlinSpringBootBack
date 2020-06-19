package com.btm.back.vo

import com.btm.back.dto.UserFiles

class PostVO {
    var id: Int? = null
    var userId: Int? = null
    var postTitle:String? = null
    var postDetail:String? = null
    var postAddress:String? = null
    var postPublic:Boolean? = null
    var postStarts:Int? = null
    var postImages:List<UserFiles>? = null
    override fun toString(): String {
        return "PostVO(id=$id, userId=$userId, postTitle=$postTitle, postDetail=$postDetail, postAddress=$postAddress, postPublic=$postPublic, postStarts=$postStarts, postImages=$postImages)"
    }


}
