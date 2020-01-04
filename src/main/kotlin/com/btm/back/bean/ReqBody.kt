package com.btm.back.bean

import com.btm.back.utils.abstractObjectToString

data class ReqBody( var phone: String ,
                    var password: String ,
                    var novelName:String,
                    var novelId:String,
                    var page:Int ,
                    var pagesize:Int ,
                    var type:Int ,
                    var id:Int ,
                    var article_Id: Int,
                    var article_Title: String,
                    var article_Creattime: Long,
                    var article_Updatetime: Long,
                    var article_Author: String,
                    var article_AuthorId: Int,
                    var article_Type: Int,
                    var article_Address_Id: Int,
                    var article_Typename: String,
                    var article_Carry_Number: Int,
                    var article_Relase_Name: String,
                    var article_State: String) : abstractObjectToString() {


}
