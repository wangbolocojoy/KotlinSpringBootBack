package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable

data class MessageBody(
                    var postMessage:String?,
                    var replyNickName:String?,
                    var userId:Int?,
                    var id:Int?,
                    var postId:Int?,
                    var msgId:Int?,
                    var replyUserId:Int?,
                    var postMsgId:Int?,
                    var messageStart:Int?): abstractObjectToString(), Serializable
