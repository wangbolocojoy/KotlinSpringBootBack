package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString
import java.io.Serializable

data class ReqBody( var phone: String? ,
                    var password: String? ,
                    var novelName:String?,
                    var relasename: String?,
                    var nickname: String? ,
                    var account: String? ,
                    var icon: String? ,
                    var likestarts: Int? ,
                    var fances: Int? ,
                    var token: String? ,
                    var novelId:String?,
                    var page:Int ?,
                    var pagesize:Int? ,
                    var type:Int? ,
                    var uploadType: String?,
                    var id:Int? ,
                    var article_Id: Int?,
                    var article_Title: String?,
                    var article_Creattime: Long?,
                    var article_Updatetime: Long?,
                    var article_Author: String?,
                    var article_AuthorId: Int?,
                    var article_Type: Int?,
                    var article_Address_Id: Int?,
                    var article_Typename: String?,
                    var article_Carry_Number: Int?,
                    var article_Relase_Name: String?,
                    var article_State: String?) : abstractObjectToString(),Serializable {


}
