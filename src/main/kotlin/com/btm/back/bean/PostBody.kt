package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable

data class PostBody(var postTitle: String? ,
                    var postDetail:String?,
                    var postAddress: String?,
                    var postImages: ArrayList<PostImagesBody>? ,
                    var postPublic:Boolean?,
                    var userId:Int?,
                    var postId:Int?,
                    var msgId:Int?,
                    var page:Int?,
                    var pageSize:Int?,
                    var postStart:Int?,
                    var longitude:String?,
                    var latitude:String?,
                    var postimagelist:List<PostImageBody>?): abstractObjectToString(),Serializable
data class PostImageBody( var userId: Int? ,
                          var postId:Int? ,
                          var fileType: String? ,
                          var originalFileName: String? ,
                          var fileUrl:String?,
                          var fileLikes:Int? ): abstractObjectToString(),Serializable
