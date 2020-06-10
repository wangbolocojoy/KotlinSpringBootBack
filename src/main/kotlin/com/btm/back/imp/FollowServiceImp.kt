package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.Follow
import com.btm.back.dto.User
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.FollowRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.FollowService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.FollowVO
import com.btm.back.vo.UserVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FollowServiceImp : FollowService {

    @Autowired
    lateinit var followRespository: FollowRespository
    @Autowired
    lateinit var userRespository: UserRespository
    private val logger: Logger = LoggerFactory.getLogger(FollowServiceImp::class.java)
    /**
     * 获取关注列表
     */
    override fun getFollowList(body: ReqBody): BaseResult {
        val follow = body.userid?.let { followRespository.findByUserid(it) }
        if (follow.isNullOrEmpty()) {
            return BaseResult.FAIL("关注列表为空")
        } else {
            val list = ArrayList<FollowVO>()
            follow.forEach {
                val user = userRespository.findById(it.followid ?: 0)
                //改为全部关注状态
                user?.isfollow = true
                if (user != null) {
                    val s = CopierUtil.copyProperties(user, FollowVO::class.java)
                    s?.let { it1 -> list.add(it1) }
                }
            }
            return BaseResult.SECUESS(list)
        }
    }

    /**
     * 获取粉丝列表
     * 返回该粉丝是否关注自己的  状态
     */
    override fun getFanceList(body: ReqBody): BaseResult {
        val fancelist = body.userid?.let { followRespository.findByFollowid(it) }
        return if (fancelist.isNullOrEmpty()) {
            BaseResult.FAIL("粉丝列表为空")
        } else {
            val list = ArrayList<FollowVO>()
            fancelist.forEach {
                val user = userRespository.findById(it.userid ?: 0)
                val followlist = body.userid?.let { followRespository.findByUserid(body.userid ?: 0) }
                //该粉丝状态是否关注了用户
                user?.isfollow = followlist?.any { it1 ->
                    it1.followid == user?.id
                }
                if (user != null) {
                    //重新包装user
                    val s = CopierUtil.copyProperties(user, FollowVO::class.java)
                    s?.let { it1 -> list.add(it1) }
                }
            }
            BaseResult.SECUESS(list)
        }
    }

    /**
     * 关注用户
     */
    override fun followUser(body: ReqBody): BaseResult {
        if (body.followid == null) {
            return BaseResult.FAIL("关注人id不能为空")
        }
        val follow = body.userid?.let { followRespository.findByUserid(it) }
        val user = userRespository.findById(body.userid?:0)
        val followuser = userRespository.findById(body.followid?:0)
        val f = follow?.any { it.followid == body.followid }
        return if (f == true) {
            BaseResult.FAIL("已经关注该用户")
        } else {
            val follow1 = Follow()
            follow1.followid = body.followid
            follow1.userid = body.userid
            var follows = user?.follows ?:0
            user?.follows = follows +1
            user?.let { userRespository.save(it) }
            var fances = followuser?.fances?:0
            followuser?.fances =  fances +1
            followuser?.let { userRespository.save(it) }
            followRespository.save(follow1)
            BaseResult.SECUESS("关注成功",user)
        }

    }

    /**
     * 取消关注
     */
    override fun unFollowUser(body: ReqBody): BaseResult {
        if (body.followid == null) {
            return BaseResult.FAIL("要取消关注人的id不能为空")
        }
        val follow = body.userid?.let { followRespository.findByUserid(it) }
        val f = follow?.singleOrNull { it.followid == body.followid }
        return if (f != null) {
            val user = userRespository.findById(body.userid?:0)
            var follownum = user?.follows ?:0
            user?.follows = follownum -1
            user?.let { userRespository.save(it) }
            val followuser = userRespository.findById(body.followid?:0)
            var fancesnum =  followuser?.fances?:0
            followuser?.fances = fancesnum -1
            followuser?.let { userRespository.save(it) }
            followRespository.delete(f)
            BaseResult.SECUESS("取消关注成功",user)
        } else {
            BaseResult.FAIL("还未关注该用户")
        }
    }

    override fun getRecommend(body: ReqBody): BaseResult {
        val follow = body.userid?.let { followRespository.findByUserid(it) }
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pagesize ?: 10)
        val pages: Page<User> = userRespository.findAll(pageable)
        var list =  pages.filterNot {
            it.id == body.userid
        }
        val u =ArrayList<FollowVO>()
        if (follow.isNullOrEmpty()){
            list.map { it.isfollow = false
                val s = CopierUtil.copyProperties(it, FollowVO::class.java)
                s?.let { it1 -> u.add(it1) }
            }
            return BaseResult.SECUESS(u)
        }else{
          follow.forEach {
              list = list.filterNot { it1->
                  it1.id == it.followid
              }
          }

            list.map {

                val s = CopierUtil.copyProperties(it, FollowVO::class.java)
                s?.let { it1 -> u.add(it1) }

            }

            return BaseResult.SECUESS(u)
        }














//        val iterator: MutableIterator<User> = pages.iterator()
//        val users=ArrayList<FollowVO>()
//        if (follow.isNullOrEmpty()){
//            iterator.forEach {
//                if (body.userid != it.id){
//                    val s = CopierUtil.copyProperties(it, FollowVO::class.java)
//                    s?.let { it2 -> users.add(it2) }
//                }
//
//            }
//            return BaseResult.SECUESS(users)
//        }else{
//            follow.forEach { iterator.forEach { it1 ->
//                if (it.userid != it1.id){
//                    val s = CopierUtil.copyProperties(it1, FollowVO::class.java)
//                    s?.let { it2 -> users.add(it2) }
//                }
//            }
//            }
//            if (users.isNullOrEmpty()){
//                return  BaseResult.FAIL("推荐列表为空")
//            }else{
//                return  BaseResult.SECUESS(users)
//            }
//        }

    }

    override fun getuserfancesandfollows(body: ReqBody): BaseResult {
        val user = userRespository.findById(body.userid ?: 0)
        return if (user != null) {
            user.fances = followRespository.findByFollowid(body.userid ?:0).size
            user.follows = followRespository.findByUserid(body.userid ?: 0).size
            userRespository.save(user)
            val s = CopierUtil.copyProperties(user, UserVo::class.java)
            BaseResult.SECUESS(s)
        }else{
            BaseResult.FAIL("该用户不存在")
        }
    }


}
