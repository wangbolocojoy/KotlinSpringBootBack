package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable


data class PageBody(var page: Int?,
                    var type: Int?,
                    var pageSize: Int?,
                    var novelName: String?,
                    var userId:Int? ,
                    var postId:Int?) : abstractObjectToString(), Serializable
