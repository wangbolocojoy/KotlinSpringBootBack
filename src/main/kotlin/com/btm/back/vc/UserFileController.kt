package com.btm.back.vc

import com.btm.back.bean.PageBody
import com.btm.back.imp.UserFilesServiceImpl
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/file/"])
class UserFileController {

    @Autowired
    lateinit var userFilesServiceImp: UserFilesServiceImpl

    @RequestMapping(value = ["upLoadFiles"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun upLoadFiles( userId:Int,latitude:String?,longitude:String?, postPublic:Boolean,postDetail:String, postAddress:String?,uploadType:String, uploadFiles:ArrayList<MultipartFile>? ) = userFilesServiceImp.uploadFiles(userId,longitude,longitude,postPublic,postDetail,postAddress,uploadType,uploadFiles)



    @UserLoginToken
    @RequestMapping(value = ["getMyAllImages"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getMyAllImages(@RequestBody body: PageBody) = userFilesServiceImp.getMyAllImages(body)

}
