package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.service.FollowService
import com.btm.back.utils.PassToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/swiftTemplate/Follow"])
class FollowController {
    @Autowired
    lateinit var  followService: FollowService


    @PassToken
    @RequestMapping(value = ["followuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun followuser(@Valid @RequestBody u: ReqBody) = followService.followUser(u)

    @PassToken
    @RequestMapping(value = ["unfollowuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun unfollowuser(@Valid @RequestBody u: ReqBody) = followService.unFollowUser(u)

    @PassToken
    @RequestMapping(value = ["getfancelist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfancelist(@Valid @RequestBody u: ReqBody) = followService.getFanceList(u)

    @PassToken
    @RequestMapping(value = ["getfollowlist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfollowlist(@Valid @RequestBody u: ReqBody) = followService.getFollowList(u)
}
