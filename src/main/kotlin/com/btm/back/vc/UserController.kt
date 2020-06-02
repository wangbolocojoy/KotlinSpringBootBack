package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.UserServiceImp
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping(value = ["/myApplication"])
class UserController  {
    @Autowired
    lateinit var userServiceImp :UserServiceImp
    @PassToken
    @RequestMapping(value = ["cas/register"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun register(@Valid @RequestBody u: ReqBody) = userServiceImp.register(u)
    @PassToken
    @RequestMapping(value = ["cas/login"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun login(@Valid @RequestBody u: ReqBody)= userServiceImp.login(u)

    @RequestMapping(value = ["cas/test"])
    @Throws(java.lang.Exception::class)
    private fun test()=userServiceImp.test()
    @UserLoginToken
    @RequestMapping(value = ["cas/deleteall"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun deleteall(@RequestBody u: ReqBody) {
    }


}
