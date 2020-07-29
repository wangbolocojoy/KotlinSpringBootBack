package com.btm.back

import com.alibaba.fastjson.JSON
import com.aliyuncs.AcsResponse
import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.IAcsClient
import com.aliyuncs.RpcAcsRequest
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.exceptions.ServerException
import com.aliyuncs.imageaudit.model.v20191230.ScanImageRequest
import com.aliyuncs.imageaudit.model.v20191230.ScanImageResponse
import com.aliyuncs.profile.DefaultProfile
import com.btm.back.utils.OSSClientConstants
import java.util.*


object test1 {

    @JvmStatic
    fun main(args: Array<String>) { //


        val url = "https://myiosandroidkotlinapplication.oss-cn-chengdu.aliyuncs.com/fileserver/IMG_3010.jpg"
//        checkScanImage(url = url)
    }


}
