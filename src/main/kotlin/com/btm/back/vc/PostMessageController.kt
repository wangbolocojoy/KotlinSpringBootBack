package com.btm.back.vc

import com.btm.back.bean.MessageBody
import com.btm.back.bean.PageBody
import com.btm.back.imp.PostMessageServiceImpl
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/Message"])
class PostMessageController {
    @Autowired
    lateinit  var postMessageServiceImp: PostMessageServiceImpl


    @UserLoginToken
    @RequestMapping(value = ["getMessages"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getMessages(@RequestBody body: PageBody) = postMessageServiceImp.getPostMessagesByPostId(body)


    @UserLoginToken
    @RequestMapping(value = ["sendMessage"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun sendMessage(@RequestBody body: MessageBody) = postMessageServiceImp.sendMessage(body)


    @UserLoginToken
    @RequestMapping(value = ["deleteMessage"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun deleteMessage(@RequestBody body: MessageBody) = postMessageServiceImp.deleteMessage(body)

    @UserLoginToken
    @RequestMapping(value = ["getUserMessages"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getUserMessages(@RequestBody body: PageBody) = postMessageServiceImp.getMyMassages(body)

    @UserLoginToken
    @RequestMapping(value = ["startMassage"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun startMassage(@RequestBody body: MessageBody) = postMessageServiceImp.startMassage(body)

    @UserLoginToken
    @RequestMapping(value = ["unStartMassage"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun unStartMassage(@RequestBody body: MessageBody) = postMessageServiceImp.unStartMassage(body)



}
