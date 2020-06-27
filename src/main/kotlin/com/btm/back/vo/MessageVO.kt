package com.btm.back.vo

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
    var messageStart:Int? = 0
}
