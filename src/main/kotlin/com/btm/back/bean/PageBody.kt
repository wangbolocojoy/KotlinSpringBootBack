package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable


data class PageBody(var page: Int?,
                    var type: Int?,
                    var pagesize: Int?,
                    var novelname: String?,
                    var userid:Int? ,
                    var postid:Int?) : abstractObjectToString(), Serializable
