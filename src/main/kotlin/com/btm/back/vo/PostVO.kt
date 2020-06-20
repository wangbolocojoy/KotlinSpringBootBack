package com.btm.back.vo


class PostVO {
    var id: Int? = null
    var userId: Int? = null
    var postTitle:String? = null
    var postDetail:String? = null
    var postAddress:String? = null
    var postPublic:Boolean? = null
    var postStarts:Int? = null
    var author:PostAuthorVo? = null
    var postImages:List<UserFilesVO>? = null
    override fun toString(): String {
        return "PostVO(id=$id, userId=$userId, postTitle=$postTitle, postDetail=$postDetail, postAddress=$postAddress, postPublic=$postPublic, postStarts=$postStarts, postImages=$postImages)"
    }


}
