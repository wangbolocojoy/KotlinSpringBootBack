package com.btm.back.vc

import com.btm.back.bean.PageBody
import com.btm.back.bean.PostBody
import com.btm.back.bean.RestPostBody
import com.btm.back.imp.PostServiceIml
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/Post"])
class PostController {
    @Autowired
    lateinit var postServiceIml: PostServiceIml

    @UserLoginToken
    @RequestMapping(value = ["sendPost"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun sendPost(@RequestBody body: PostBody,@RequestPart("uploadFiles")uploadFiles: ArrayList<MultipartFile>) = postServiceIml.sendPost(body,uploadFiles)

    @UserLoginToken
    @RequestMapping(value = ["getPostsByUserId"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getPostsByUserId(@RequestBody body: PageBody) = postServiceIml.getPostByUserId(body)

    @UserLoginToken
    @RequestMapping(value = ["getMyPosts"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getMyPosts(@RequestBody body: PageBody) = postServiceIml.getMyPosts(body)


    @UserLoginToken
    @RequestMapping(value = ["updatePosts"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun updatePosts(@RequestBody body: PageBody) = postServiceIml.updatePosts(body)

    @UserLoginToken
    @RequestMapping(value = ["reportPostByPostId"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun reportPostByPostId(@RequestBody body: RestPostBody) = postServiceIml.reportPostByPostId(body)

    @UserLoginToken
    @RequestMapping(value = ["getReportList"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getReportList(@RequestBody body: PageBody) = postServiceIml.getReportList(body)

    @UserLoginToken
    @RequestMapping(value = ["getExamineList"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getExamineList(@RequestBody body: PageBody) = postServiceIml.getExamineList(body)

    @PassToken
    @RequestMapping(value = ["gettoken"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun gettoken() = postServiceIml.gettoken()


    @PassToken
    @RequestMapping(value = ["getPosts"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getPosts(@RequestBody body: PageBody) = postServiceIml.getPosts(body)

    @UserLoginToken
    @RequestMapping(value = ["deletePost"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun deletePost(@RequestBody body: PageBody) = postServiceIml.deletePost(body)

    @PassToken
    @RequestMapping(value = ["isHaveNewPost"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun isHaveNewPost(@RequestBody body: PageBody) = postServiceIml.isHaveNewPost(body)


}
