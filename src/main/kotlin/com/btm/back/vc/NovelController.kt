package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.NovelServiceImpl
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/novel"])
class NovelController {


    @Autowired
    lateinit var novelServiceImp: NovelServiceImpl

    @RequestMapping(value = ["/cas/getPageNovelList"],method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun getPageNovelList(@RequestBody p: ReqBody?)= novelServiceImp.getPageNovelList(p)

    /**
     * 搜索小说
     *
     * @param response
     * @param novelname
     * @throws Exception
     */
    @UserLoginToken
    @RequestMapping(value = ["/cas/searchNovel"],method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun searchNovel( @RequestBody p: ReqBody?) =novelServiceImp.searchNovel(p)




}
