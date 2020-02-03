package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.NovelServiceImp
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/myApplication"])
class NovelController {


    @Autowired
    lateinit var novelServiceImp: NovelServiceImp

    @UserLoginToken
    @RequestMapping(value = ["/cas/getPageNovelList"])
    @Throws(Exception::class)
    fun getPageNovelList( @RequestBody p: ReqBody?)= novelServiceImp.getPageNovelList(p)

    /**
     * 搜索小说
     *
     * @param response
     * @param novelname
     * @throws Exception
     */
    @UserLoginToken
    @RequestMapping(value = ["/cas/searchNovel"])
    @Throws(Exception::class)
    fun searchNovel( @RequestParam novelName: String?) =novelServiceImp.searchNovel(novelName)




}
