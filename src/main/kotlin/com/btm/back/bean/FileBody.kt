package com.btm.back.bean

import java.io.InputStream

data class FileBody(
        var filename: String? = null ,
        var file: InputStream? ,
        var suffix: String? = null,
        var type:String?= null,
        var user:String?= null,
        var userid: String? = "",
        var fileurl:String?="",
        var fileLikes:Int =0,
        var fileseenum:Int =0,
        var fileispublic:Boolean =false
)