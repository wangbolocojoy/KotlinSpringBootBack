package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.ArticleServiceImp
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/myApplication"])
class ArticleController {

    @Autowired
    lateinit var articleServiceImp: ArticleServiceImp

    @UserLoginToken
    @RequestMapping(value = ["/cas/getArticleList"],method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getArticleList(@RequestBody s: ReqBody?) = articleServiceImp.getArticleList(s)

    @UserLoginToken
    @RequestMapping(value = ["/cas/creatArticle"],method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun creatArticle(@RequestBody s: ReqBody?) = articleServiceImp.creatArticle(s)

    @UserLoginToken
    @RequestMapping(value = ["/cas/getArticleDetailById"],method = [RequestMethod.GET])
    @Throws(java.lang.Exception::class)
    fun getArticleDetailById(@RequestBody s: ReqBody?) = articleServiceImp.getArticleDetailById(s)

    @UserLoginToken
    @RequestMapping(value = ["/cas/getArticleByType"],method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getArticleByType(@RequestBody s: ReqBody?) = articleServiceImp.getArticleByType(s)


    @UserLoginToken
    @RequestMapping(value = ["/cas/updateArticle"],method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun updateArticle(@RequestBody s: ReqBody?) = articleServiceImp.updateArticle(s)


    @UserLoginToken
    @RequestMapping(value = ["/cas/deleteArticleById"],method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun deleteArticleById(@RequestBody s: ReqBody?) = articleServiceImp.deleteArticleById(s)


}
