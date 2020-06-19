package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable

data class PostBody(var posttitle: String? ,
                    var postdetail:String?,
                    var postaddress: String?,
                    var postimages: ArrayList<PostImagesBody>? ,
                    var postpulic:Boolean?,
                    var userid:Int?,
                    var poststart:Int?): abstractObjectToString(),Serializable
