package com.btm.back.vo

import java.util.*


class PostVO {
    var id: Int? = null
    var userId: Int? = null
    var postDetail:String? = null
    var postAddress:String? = null
    var postPublic:Boolean? = null
    var creatTime: Date? = null
    var postStarts:Int? = 0
    var author:PostAuthorVo? = null
    var postImages:List<UserFilesVO>? = null
    var latitude:String? = null
    var longitude:String? = null
    var isStart:Boolean? = false
    var isCollection:Boolean? = false
    var postMessageNum:Int? =0
    var msgNum:Int? = 0
    override fun toString(): String {
        return "PostVO(id=$id, userId=$userId, postDetail=$postDetail, postAddress=$postAddress, postPublic=$postPublic, creatTime=$creatTime, postStarts=$postStarts, author=$author, postImages=$postImages, latitude=$latitude, longitude=$longitude, isStart=$isStart, isCollection=$isCollection, msgNum=$msgNum)"
    }



}
