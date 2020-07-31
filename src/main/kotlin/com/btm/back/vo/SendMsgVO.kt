package com.btm.back.vo

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*

class SendMsgVO {
    var id: Int? = null
    var userId: Int? = null
    var postId:Int? = null
    var userIcon:String? = null
    var userNickName:String? = null
    var message: String? = null
    var postMsgCreatTime: Date? = null
    var messageStart:Int? = null
    override fun toString(): String {
        return "SendMsgVO(id=$id, userId=$userId, postId=$postId, userIcon=$userIcon, userNickName=$userNickName, message=$message, postMsgCreatTime=$postMsgCreatTime, messageStart=$messageStart)"
    }

}
