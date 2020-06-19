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
    fun upLoadFiles(@RequestParam id:Int, @RequestParam postId:Int,@RequestParam uploadtype:String, uploadFiles:ArrayList<MultipartFile>? ) = userFilesServiceImp.uploadFiles(id,postId,uploadtype,uploadFiles)


    @UserLoginToken
    @RequestMapping(value = ["upLoadFiless"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun upLoadFiless( id:Int,  postId:Int, uploadtype:String, uploadFiles:ArrayList<MultipartFile>? ) = userFilesServiceImp.uploadFiles(id,postId,uploadtype,uploadFiles)



}
