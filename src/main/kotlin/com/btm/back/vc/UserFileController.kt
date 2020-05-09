package com.btm.back.vc

import com.btm.back.bean.FileBody
import com.btm.back.bean.ReqBody
import com.btm.back.imp.UserFilesServiceImp
import com.btm.back.imp.UserServiceImp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/myApplication"])
class UserFileController {

    @Autowired
    lateinit var userFilesServiceImp: UserFilesServiceImp

    @RequestMapping(value = ["cas/updateloadfile"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun updateloadfile(@RequestBody b: FileBody,filelist :MultipartFile) = userFilesServiceImp.saveFiles(b)
}