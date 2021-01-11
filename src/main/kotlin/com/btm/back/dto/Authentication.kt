package com.btm.back.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*


@Entity
@Table(name = "Authentication")
@JsonIgnoreProperties(value = ["handler", "hibernateLazyInitializer", "fieldHandler"])
class Authentication{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    var id: Int? = null
    var userId: Int? = null
    var Name:String? = null //姓名（人像面）
    var Sex:String? = null //性别（人像面）
    var Nation:String? = null //	民族（人像面）
    var Birth:String? = null //出生日期（人像面）
    var Address:String? = null //地址（人像面）
    var IdNum:String? =null //身份证号（人像面）
    var Authority:String? =null //发证机关（国徽面）
    var ValidDate:String? =null //证件有效期（国徽面）
    var startDate:String? =null //证件有效期（国徽面）
    var endDate:String? =null //证件有效期（国徽面）
    var AdvancedInfo:String? =null
    var RequestId:String? =null //唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
    var FrontIdCard:String? = null//身份证正面
    var NationalIdCard:String? = null//身份证反面
    var authentication:Boolean? = null


}
