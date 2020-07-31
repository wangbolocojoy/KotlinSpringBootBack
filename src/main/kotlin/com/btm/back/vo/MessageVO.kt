package com.btm.back.vo

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*

class MessageVO {
    var id: Int? = null
    var userId: Int? = null
    var postId:Int? = null
    var userIcon:String? = null
    var userNickName:String? = null
    var chiledMessage:List<MessageVO>? = null
    var message: String? = null
    var postMsgCreatTime: Date? = null
    var isStart:Boolean? = null
    var messageStart:Int? = null
    var replyUserId:Int? = null
    var replyNickName:String? = null
    override fun toString(): String {
        return "MessageVO(id=$id, userId=$userId, postId=$postId, userIcon=$userIcon, userNickName=$userNickName, chiledMessage=$chiledMessage, message=$message, postMsgCreatTime=$postMsgCreatTime, messageStart=$messageStart, replyUserId=$replyUserId, replyNickName=$replyNickName)"
    }


}
