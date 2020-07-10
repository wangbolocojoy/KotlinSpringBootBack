package com.btm.back.vc

import com.btm.back.bean.PageBody
import com.btm.back.bean.PostBody
import com.btm.back.imp.PostServiceIml
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/swiftTemplate/Post"])
class PostController {
    @Autowired
    lateinit var postServiceIml: PostServiceIml

    @UserLoginToken
    @RequestMapping(value = ["sendPost"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun sendPost(@RequestBody body: PostBody) = postServiceIml.sendPost(body)

    @UserLoginToken
    @RequestMapping(value = ["getPostsByUserId"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getPostsByUserId(@RequestBody body: PageBody) = postServiceIml.getPostByUserId(body)


    @UserLoginToken
    @RequestMapping(value = ["getPosts"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getPosts(@RequestBody body: PageBody) = postServiceIml.getPosts(body)

    @UserLoginToken
    @RequestMapping(value = ["deletePost"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun deletePost(@RequestBody body: PageBody) = postServiceIml.deletePost(body)

}
