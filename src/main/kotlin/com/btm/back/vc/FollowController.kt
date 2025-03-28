package com.btm.back.vc

import com.btm.back.bean.ReqBody
import com.btm.back.imp.FollowServiceImpl
import com.btm.back.service.FollowService
import com.btm.back.utils.UserLoginToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * 关注--粉丝
 */
@RestController
@RequestMapping(value = ["/api/Follow"])
class FollowController {

    @Autowired
    lateinit var  followServiceImp: FollowServiceImpl


    @UserLoginToken
    @RequestMapping(value = ["followuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun followuser(@Valid @RequestBody u: ReqBody) = followServiceImp.followUser(u)

    @UserLoginToken
    @RequestMapping(value = ["unfollowuser"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun unfollowuser(@Valid @RequestBody u: ReqBody) = followServiceImp.unFollowUser(u)

    @UserLoginToken
    @RequestMapping(value = ["getfancelist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfancelist(@Valid @RequestBody u: ReqBody) = followServiceImp.getFanceList(u)

    @UserLoginToken
    @RequestMapping(value = ["getfollowlist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfollowlist(@Valid @RequestBody u: ReqBody) = followServiceImp.getFollowList(u)

    @UserLoginToken
    @RequestMapping(value = ["getrecommendlist"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getrecommendlist(@Valid @RequestBody u: ReqBody) = followServiceImp.getRecommend(u)

    @UserLoginToken
    @RequestMapping(value = ["getfancesandfollows"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    private fun getfancesandfollows(@Valid @RequestBody u: ReqBody) = followServiceImp.getuserfancesandfollows(u)

}
