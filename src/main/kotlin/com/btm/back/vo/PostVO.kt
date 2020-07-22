package com.btm.back.vo

import com.btm.back.dto.ReportPost
import com.fasterxml.jackson.annotation.JsonTypeInfo


class PostVO {
    var id: Int? = null
    var userId: Int? = null
    var postDetail:String? = null
    var postAddress:String? = null
    var postPublic:Boolean? = null
    var creatTime: String? = null
    var postStarts:Int? = 0
    var author:PostAuthorVo? = null
    var postImages:List<UserFilesVO>? = null
    var postReports:List<ReportPost>?  = null
    var latitude:String? = null
    var longitude:String? = null
    var isStart:Boolean? = false
    var isCollection:Boolean? = false
    var postState:Int? = 0
    var postReport:Int? = 0
    var postMessageNum:Int? =0
    var msgNum:Int? = 0
    override fun toString(): String {
        return "PostVO(id=$id, userId=$userId, postDetail=$postDetail, postAddress=$postAddress, postPublic=$postPublic, creatTime=$creatTime, postStarts=$postStarts, author=$author, postImages=$postImages, postReports=$postReports, latitude=$latitude, longitude=$longitude, isStart=$isStart, isCollection=$isCollection, postState=$postState, postReport=$postReport, postMessageNum=$postMessageNum, msgNum=$msgNum)"
    }


}
