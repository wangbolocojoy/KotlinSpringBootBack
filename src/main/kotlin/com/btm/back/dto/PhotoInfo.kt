
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





}