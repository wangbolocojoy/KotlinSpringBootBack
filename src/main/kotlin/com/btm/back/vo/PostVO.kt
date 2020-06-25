package com.btm.back.vo



class PostVO {
    var id: Int? = null
    var userId: Int? = null
    var postDetail:String? = null
    var postAddress:String? = null
    var postPublic:Boolean? = null
    var creatTime:Long? = null
    var postStarts:Int? = null
    var author:PostAuthorVo? = null
    var postImages:List<UserFilesVO>? = null
    override fun toString(): String {
        return "PostVO(id=$id, userId=$userId, postDetail=$postDetail, postAddress=$postAddress, postPublic=$postPublic, creatTime=$creatTime, postStarts=$postStarts, author=$author, postImages=$postImages)"
    }


}
