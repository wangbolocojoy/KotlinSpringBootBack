package com.btm.back.vo

import java.util.*

class FeedBackVO {

    var id: Int? = null
    var userId: Int? = null
    var userMsg: String? = null
    var backTime: Date? = null
    var userNickName:String? = null
    var userIcon:String? = null
    override fun toString(): String {
        return "FeedBackVO(id=$id, userId=$userId, userMsg=$userMsg, backTime=$backTime, userNickName=$userNickName, userIcon=$userIcon)"
    }

}
