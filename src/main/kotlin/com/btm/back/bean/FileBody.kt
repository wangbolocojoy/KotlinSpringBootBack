package com.btm.back.bean

import java.io.InputStream

data class FileBody(
        var fileName: String? = null ,
        var file: InputStream? ,
        var suffix: String? = null,
        var type:String?= null,
        var user:String?= null,
        var userId: String? = "",
        var fileUrl:String?="",
        var fileLikes:Int =0,
        var fileIsPublic:Boolean =false
)
