package com.btm.back.vo

import com.btm.back.dto.PostMessage

class MessageVO {
    var id: Int? = null
    var userId: Int? = null
    var postId:Int? = null
    var chiledMessage:List<PostMessage>? = null
    var postPostMessage: String? = null
    var postMsgCreatTime: String? = null

}
