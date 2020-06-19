package com.btm.back.vo

class UserVO {
    var id: Int? = null
    var relaseName: String? = null
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
    var esayInfo:String? = null
    var address:String? = null
    override fun toString(): String {
        return "UserVO(id=$id, relaseName=$relaseName, phone=$phone, nickName=$nickName, account=$account, userSex=$userSex, icon=$icon, likeStarts=$likeStarts, postNum=$postNum, fances=$fances, follows=$follows, token=$token, esayInfo=$esayInfo, address=$address)"
    }


}