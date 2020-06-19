package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable

data class PostBody(var postTitle: String? ,
                    var postDetail:String?,
                    var postAddress: String?,
                    var postImages: ArrayList<PostImagesBody>? ,
                    var postPulic:Boolean?,
                    var userId:Int?,
                    var postStart:Int?): abstractObjectToString(),Serializable