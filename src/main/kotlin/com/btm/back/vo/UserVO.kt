package com.btm.back.vo

import com.fasterxml.jackson.annotation.JsonTypeInfo
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
class UserVO {
    var id: Int? = null
    var realName: String? = null
    var phone: String? = null
    var nickName: String? = null
    var account: String? = null
    var userSex:Boolean? = false
    var icon: String? = null
    var likeStarts: Int? = 0
    var postNum:Int? = 0
    var fances: Int? = 0
    var follows: Int? = 0
    var token: String? = null
    var easyInfo:String? = null
    var address:String? = null
    var creatTime: String? = null
    var birthDay: String? = null
    var constellation:String? = null
    var province:String? = null
    var city:String? = null
    var isItBanned:Boolean = false
    var isAdministrators:Boolean = false
    override fun toString(): String {
        return "UserVO(id=$id, realName=$realName, phone=$phone, nickName=$nickName, account=$account, userSex=$userSex, icon=$icon, likeStarts=$likeStarts, postNum=$postNum, fances=$fances, follows=$follows, token=$token, easyInfo=$easyInfo, address=$address, creatTime=$creatTime, birthDay=$birthDay, constellation=$constellation, province=$province, city=$city, isItBanned=$isItBanned, isAdministrators=$isAdministrators)"
    }


}
