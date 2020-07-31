package com.btm.back.vo

class UserVO {
    var id: Int? = null
    var realName: String? = null
    var phone: String? = null
    var nickName: String? = null
    var account: String? = null
    var userSex:Boolean? = null
    var icon: String? = null
    var likeStarts: Int? = null
    var postNum:Int? = null
    var fances: Int? = null
    var follows: Int? = null
    var token: String? = null
    var easyInfo:String? = null
    var address:String? = null
    var creatTime: String? = null
    var birthDay: String? = null
    var constellation:String? = null
    var province:String? = null
    var city:String? = null
    var isbanned:Boolean? = null
    var administrators:Boolean? = null
    var authentication:Boolean? = null

    override fun toString(): String {
        return "UserVO(id=$id, realName=$realName, phone=$phone, nickName=$nickName, account=$account, userSex=$userSex, icon=$icon, likeStarts=$likeStarts, postNum=$postNum, fances=$fances, follows=$follows, token=$token, easyInfo=$easyInfo, address=$address, creatTime=$creatTime, birthDay=$birthDay, constellation=$constellation, province=$province, city=$city, isbanned=$isbanned, administrators=$administrators, authentication=$authentication)"
    }


}
