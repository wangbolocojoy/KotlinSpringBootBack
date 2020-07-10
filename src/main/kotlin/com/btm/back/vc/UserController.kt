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
    @RequestMapping(value = ["sendMsg"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun sendmsg(@Valid @RequestBody u: ReqBody) = userServiceImp.sendmsg(u)

    @PassToken
    @RequestMapping(value = ["login"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun login(@Valid @RequestBody u: ReqBody)= userServiceImp.login(u)


    @PassToken
    @RequestMapping(value = ["updatePassWord"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun updatePassWord(@Valid @RequestBody u: ReqBody)= userServiceImp.updatePassWord(u)

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



    @UserLoginToken
    @RequestMapping(value = ["deleteall"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun deleteall(@RequestBody u: ReqBody) {}

    @UserLoginToken
    @RequestMapping(value = ["searchfollow"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun searchfollow(@RequestBody u: ReqBody) =userServiceImp.searchfollow(u)

    @UserLoginToken
    @RequestMapping(value = ["sendFeedBack"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun sendFeedBack(@RequestBody u: ReqBody) =userServiceImp.searchfollow(u)

    @UserLoginToken
    @RequestMapping(value = ["getFeedBack"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getFeedBack(@RequestBody u: ReqBody) =userServiceImp.searchfollow(u)

}
