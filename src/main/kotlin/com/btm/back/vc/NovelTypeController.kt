package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.NovelTypeServiceImpl
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/novel"])
class NovelTypeController {
    @Autowired
    lateinit var novelTypeServiceImp: NovelTypeServiceImpl

    @UserLoginToken
    @RequestMapping(value = ["/cas/getNovelTypes"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getNovelTypes(@RequestBody b:ReqBody?)= novelTypeServiceImp.getNovelTypes(b)
}
