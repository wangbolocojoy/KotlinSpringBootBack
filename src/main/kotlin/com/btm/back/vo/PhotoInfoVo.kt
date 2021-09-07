package com.btm.back.vo

import java.util.*

class PhotoInfoVo {


    /**
     * 照片拍摄日期
     */
    var Photoshootingtime: Date? = null
    /**
     * 照片分类id
     */
    var Photoclassificationid: Int? = null

    /**
     * 照片分类描述
     */
    var Photoclassification: String? = null

    /**
     * 照片尺寸
     */
    var ResolutionUnit: String? = null

    /**
     * 相机品牌
     */
    var Make:String? = null
    /**
     * 相机型号
     */
    var Model:String? = null
    /**
     * 镜头型号
     */
    var Lensmodel:String? = null
    /**
     * 快门时间
     */
    var ExposureTime:String? = null

    /**
     * 焦距
     */
    var  focallength:String? = null

    /**
     * ios
     */
    var ISOSpeedRatings:String? = null
    /**
     * 光圈数
     */
    var aperture:String? = null

    /**
     * 版权
     */
    var Copyright:String? = null
    /**
     * 拍摄者
     */
    var Artist:String? = null
    /**
     * 照片大小
     */
    var Photosize:String? = null

    /**
     * 照片地址
     */
    var Photourl:String? = null
    override fun toString(): String {
        return " Photoshootingtime=$Photoshootingtime, Photoclassificationid=$Photoclassificationid, Photoclassification=$Photoclassification, ResolutionUnit=$ResolutionUnit, Make=$Make, Model=$Model, Lensmodel=$Lensmodel, ExposureTime=$ExposureTime, focallength=$focallength, ISOSpeedRatings=$ISOSpeedRatings, aperture=$aperture, Copyright=$Copyright, Artist=$Artist, Photosize=$Photosize, Photourl=$Photourl)"
    }


}