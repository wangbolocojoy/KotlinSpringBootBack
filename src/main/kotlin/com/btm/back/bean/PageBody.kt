package com.btm.back.bean

import com.btm.back.dto.Post
import com.btm.back.utils.abstractObjectToString
import java.io.Serializable
import java.util.*


data class PageBody(var page: Int?,
                    var type: Int?,
                    var pageSize: Int?,
                    var novelName: String?,
                    var userId:Int? ,
                    var postState:Int? ,
                    var postReport:Int? ,
                    var postList:List<PostInfoBody>?,
                    var postId:Int?,
                    var public:Boolean?
                    ) : abstractObjectToString(), Serializable
data class RestPostBody(var postId: Int?,
                    var userId: Int? = null,
                    var reportReason:String? = null,
                    var reportDescribe:String? = null
                    ):abstractObjectToString(),Serializable

data class PostInfoBody(var postId: Int?,
                        var postState: Int? = null,
                        var postPublic:Boolean? = null
):abstractObjectToString(),Serializable

