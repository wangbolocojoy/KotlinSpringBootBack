package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable

data class MessageBody(
                    var postMessage:String?,
                    var userId:Int?,
                    var postId:Int?,
                    var postMsgId:Int?,
                    var messageStart:Int?): abstractObjectToString(), Serializable
