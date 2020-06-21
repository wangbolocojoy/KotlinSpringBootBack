package com.btm.back.vo

class PostAuthorVo {
    var nickName: String? = null
    var icon: String? = null
    var id: Int? = null
    var address: String? = null
    override fun toString(): String {
        return "PostAuthorVo(nickName=$nickName, icon=$icon, id=$id, address=$address)"
    }

}
