package com.btm.back.vc

import com.btm.back.bean.PostBody
import com.btm.back.imp.PostStartServiceIml
import com.btm.back.utils.PassToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/swiftTemplate/PostStart/"])
class PostStartController {
    @Autowired
    lateinit var postStartServiceIml: PostStartServiceIml

    @PassToken
    @RequestMapping(value = ["start"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun start(@RequestBody body: PostBody) = postStartServiceIml.start(body)

    @PassToken
    @RequestMapping(value = ["unStart"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun unStart(@RequestBody body: PostBody) = postStartServiceIml.unStart(body)
}
