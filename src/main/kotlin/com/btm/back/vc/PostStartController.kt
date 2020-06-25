package com.btm.back.vc

import com.btm.back.bean.PostBody
import com.btm.back.imp.FavoritesServiceImp
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

    @Autowired
    lateinit var favoritesServiceImp: FavoritesServiceImp
    @PassToken
    @RequestMapping(value = ["start"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun start(@RequestBody body: PostBody) = postStartServiceIml.start(body)

    @PassToken
    @RequestMapping(value = ["unStart"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun unStart(@RequestBody body: PostBody) = postStartServiceIml.unStart(body)


    @PassToken
    @RequestMapping(value = ["getPostStartList"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getPostStartList(@RequestBody body: PostBody) = postStartServiceIml.getPostStartList(body)

    @PassToken
    @RequestMapping(value = ["collection"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun collection(@RequestBody body: PostBody) = favoritesServiceImp.collection(body)

    @PassToken
    @RequestMapping(value = ["getCollectionList"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getCollectionList(@RequestBody body: PostBody) = favoritesServiceImp.getCollectionList(body)

    @PassToken
    @RequestMapping(value = ["cancelCollection"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun cancelCollection(@RequestBody body: PostBody) = favoritesServiceImp.cancelCollection(body)

}
