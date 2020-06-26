package com.btm.back.vc

import com.btm.back.bean.PageBody
import com.btm.back.imp.PostMessageServiceImp
import com.btm.back.imp.PostServiceIml
import com.btm.back.utils.PassToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/swiftTemplate/Message"])
class PostMessageController {
    @Autowired
    lateinit  var postMessageServiceImp: PostMessageServiceImp


    @PassToken
    @RequestMapping(value = ["getMessages"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getMessages(@RequestBody body: PageBody) = postMessageServiceImp.getPostMessagesByPostId(body)


    @PassToken
    @RequestMapping(value = ["sendMessage"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun sendMessage(@RequestBody body: PageBody) = postMessageServiceImp.sendMessage(body)


    @PassToken
    @RequestMapping(value = ["deleteMessage"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun deleteMessage(@RequestBody body: PageBody) = postMessageServiceImp.deleteMessage(body)

}
