package com.btm.back.vo

class AuthenticationVO {
    var id: Int? = null
    var userId: Int? = null
    var Name:String? = null //姓名（人像面）
    var Sex:String? = null //性别（人像面）
    var Nation:String? = null //	民族（人像面）
    var Birth:String? = null //出生日期（人像面）
    var Address:String? = null //地址（人像面）
    var IdNum:String? =null //身份证号（人像面）
    var Authority:String? =null //发证机关（国徽面）
    var startDate:String? =null //证件有效期（国徽面）
    var endDate:String? =null //证件有效期（国徽面）
    var FrontIdCard:String? = null//身份证正面
    var NationalIdCard:String? = null//身份证反面
    var isAuthentication :Boolean? = false
    override fun toString(): String {
        return "AuthenticationVO(id=$id, userId=$userId, Name=$Name, Sex=$Sex, Nation=$Nation, Birth=$Birth, Address=$Address, IdNum=$IdNum, Authority=$Authority, startDate=$startDate, endDate=$endDate, FrontIdCard=$FrontIdCard, NationalIdCard=$NationalIdCard, isAuthentication=$isAuthentication)"
    }


}
