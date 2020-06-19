package com.btm.back.vo

class FollowVO {
    var id: Int? = null
    var nickName: String? = null
    var account: String? = null
    var icon: String? = null
    var address:String? = null
    var userSex:Boolean? = null
    var easyInfo:String? = null
    var isFollow:Boolean? = null
    var postNum:Int? = null
    var fances: Int? = null
    var follows: Int? = null
    override fun toString(): String {
        return "FollowVO(id=$id, nickName=$nickName, account=$account, icon=$icon, address=$address, userSex=$userSex, easyInfo=$easyInfo, isFollow=$isFollow, postNum=$postNum, fances=$fances, follows=$follows)"
    }

}
