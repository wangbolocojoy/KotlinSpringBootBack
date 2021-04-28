
package com.btm.back.dto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "PhotoInfo")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class PhotoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    /**
     * 照片尺寸
     */
    var ResolutionUnit: Int? = null
    /**
     * 照片尺寸
     */
    var ExifOffset:Int? = null
    /**
     * 相机品牌
     */
    var Make:String? = null
    /**
     * 相机型号
     */
    var Model:String? = null
    /**
     * 历史软件Agent
     */
    var Software:String? = null
    /**
     * 拍摄日期
     */
    var DateTimeOriginal: Date? = null
    /**
     * 版权
     */
    var Copyright:String? = null
    /**
     * 创作者
     */
    var Artist:String? = null
    /**
     * 照片尺寸
     */
    var LensSpecification:String? = null
    /**
     * 照片尺寸
     */
    var FocalLengthIn35mmFilm:String? = null
    /**
     * 照片尺寸
     */
    var BodySerialNumber:String? = null

    /**
     * 照片尺寸
     */
    var Photosize:String? = null

    /**
     * 快门时间
     */
    var ExposureTime:Double? = null

    /**
     * 光圈
     */
    var FNumber:Double? = null

    /**
     * ios
     */
    var ISOSpeedRatings:Int? = null
}