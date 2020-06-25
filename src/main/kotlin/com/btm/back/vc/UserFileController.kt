package com.btm.back.vc

import com.btm.back.bean.FileBody
import com.btm.back.imp.UserFilesServiceImp
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/swiftTemplate/file/"])
class UserFileController {

    @Autowired
    lateinit var userFilesServiceImp: UserFilesServiceImp
    @UserLoginToken
    @RequestMapping(value = ["upLoadFile"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun upLoadFile(@RequestBody b: FileBody,filelist :MultipartFile) = userFilesServiceImp.saveFiles(b)


    @UserLoginToken
    @RequestMapping(value = ["upLoadFiles"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun upLoadFiles( userId:Int,latitude:String?,longitude:String?, postPublic:Boolean,postDetail:String, postAddress:String?,uploadType:String, uploadFiles:ArrayList<MultipartFile>? ) = userFilesServiceImp.uploadFiles(userId,longitude,longitude,postPublic,postDetail,postAddress,uploadType,uploadFiles)



}
