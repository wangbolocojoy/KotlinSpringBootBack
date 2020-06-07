package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.UserServiceImp
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid


@RestController
@RequestMapping(value = ["/swiftTemplate/User"])
class UserController  {
    @Autowired
    lateinit var userServiceImp :UserServiceImp
    @PassToken
    @RequestMapping(value = ["register"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun register(@Valid @RequestBody u: ReqBody) = userServiceImp.register(u)
    @PassToken
    @RequestMapping(value = ["login"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun login(@Valid @RequestBody u: ReqBody)= userServiceImp.login(u)

    @UserLoginToken
    @RequestMapping(value = ["getUseInfo"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getUserInfo(@Valid @RequestBody u: ReqBody)= userServiceImp.getuserinfo(u)

    @UserLoginToken
    @RequestMapping(value = ["updateUser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun updateUser(@Valid @RequestBody u: ReqBody)= userServiceImp.updateUser(u)

    @UserLoginToken
    @RequestMapping(value = ["uploadusericon"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun uploadIcon(@RequestParam  id:Int, @RequestParam uploadtype:String, @RequestPart("uploadFile")uploadFile: MultipartFile? )= userServiceImp.updateIcon(id,uploadtype,uploadFile)
    @RequestMapping(value = ["test"])
    @Throws(java.lang.Exception::class)
    private fun test()=userServiceImp.test()
    @UserLoginToken
    @RequestMapping(value = ["deleteall"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun deleteall(@RequestBody u: ReqBody) {
    }


}
