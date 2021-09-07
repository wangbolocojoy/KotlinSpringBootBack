package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

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
data class PhotoBody( var type: Int? ,var page:Int?, var pageSize:Int?,
                          ): abstractObjectToString(),Serializable

data class PhotoInfoBody(

    /**
     * 照片拍摄日期
     */
    var Photoshootingtime: Date? ,
    /**
     * 照片分类id
     */
    var Photoclassificationid: Int? ,

    /**
     * 照片分类描述
     */
    var Photoclassification: String? ,

    /**
     * 照片尺寸
     */
    var ResolutionUnit: String? ,

    /**
     * 相机品牌
     */
    var Make:String?,
    /**
     * 相机型号
     */
    var Model:String? ,
    /**
     * 镜头型号
     */
    var Lensmodel:String?,
    /**
     * 快门时间
     */
    var ExposureTime:String? ,

    /**
     * 焦距
     */
    var  focallength:String? ,

    /**
     * ios
     */
    var ISOSpeedRatings:String? ,
    /**
     * 光圈数
     */
    var aperture:String?,

    /**
     * 版权
     */
    var Copyright:String? ,
    /**
     * 拍摄者
     */
    var Artist:String?,
    /**
     * 照片大小
     */
    var Photosize:String? ,

    /**
     * 照片地址
     */
    var Photourl:String? ,

): abstractObjectToString(),Serializable