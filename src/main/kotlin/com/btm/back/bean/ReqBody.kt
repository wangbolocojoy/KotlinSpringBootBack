package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import javax.validation.constraints.NotBlank

class ReqBody : abstractObjectToString() {
    @NotBlank(message = "手机号不能为空")
    var phone: String = ""
    @NotBlank(message = "密码不能为空")
    var password: String = ""
    var novelName:String =""
    var novelId:String = ""
    var page:Int = 1
    var pagesize:Int =10
    var type:Int = 8
    var id:Int = 0
    var article_Id: Int? = null
    var article_Title: String? = null
    var article_Creattime: Long? = null
    var article_Updatetime: Long? = null
    var article_Author: String? = null
    var article_AuthorId: Int? = null
    var article_Type: Int? = null
    var article_Address_Id: Int? = null
    var article_Typename: String? = null
    var article_Carry_Number: Int? = null
    var article_Relase_Name: String? = null
    var article_State: String? = null

}
