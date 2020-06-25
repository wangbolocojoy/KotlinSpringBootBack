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
import com.btm.back.vo.UserVO
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
    * @Description: 获取关注列表
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    **/
    override fun getFollowList(body: ReqBody): BaseResult {
        val follow = body.userId?.let { followRespository.findByUserId(it) }
        if (follow.isNullOrEmpty()) {
            return BaseResult.FAIL("关注列表为空")
        } else {
            val list = ArrayList<FollowVO>()
            follow.forEach {
                val user = userRespository.findById(it.followId ?: 0)
                //改为全部关注状态
                user?.isFollow = true
                if (user != null) {
                    val s = CopierUtil.copyProperties(user, FollowVO::class.java)
                    s?.let { it1 -> list.add(it1) }
                }
            }
            logger.info("获取用户关注列表$list")
            return BaseResult.SECUESS(list)
        }
    }


   /**
   * @Description: 获取粉丝列表
   * @Param:
   * @return:
   * @Author: hero
   * @Date: 2020-06-26
   **/
    override fun getFanceList(body: ReqBody): BaseResult {
        val fancelist = body.userId?.let { followRespository.findByFollowId(it) }
        return if (fancelist.isNullOrEmpty()) {
            BaseResult.FAIL("粉丝列表为空")
        } else {
            val list = ArrayList<FollowVO>()
            fancelist.forEach {
                val user = userRespository.findById(it.userId ?: 0)
                val followlist = body.userId?.let { followRespository.findByUserId(body.userId ?: 0) }
                //该粉丝状态是否关注了用户
                user?.isFollow = followlist?.any { it1 ->
                    it1.followId == user?.id
                }
                if (user != null) {
                    //重新包装user
                    val s = CopierUtil.copyProperties(user, FollowVO::class.java)
                    s?.let { it1 -> list.add(it1) }
                }
            }
            logger.info("获取用户粉丝列表$list")
            BaseResult.SECUESS(list)
        }
    }

    /**
     * 关注用户
     */
    override fun followUser(body: ReqBody): BaseResult {
        if (body.followId == null) {
            return BaseResult.FAIL("关注人id不能为空")
        }
        val follow = body.userId?.let { followRespository.findByUserId(it) }
        val user = userRespository.findById(body.userId?:0)
        val followuser = userRespository.findById(body.followId?:0)
        val f = follow?.any { it.followId == body.followId }
        return if (f == true) {
            BaseResult.FAIL("已经关注该用户")
        } else {
            val follow1 = Follow()
            follow1.followId = body.followId
            follow1.userId = body.userId
            var follows = user?.follows ?:0
            user?.follows = follows +1
            user?.let { userRespository.save(it) }
            var fances = followuser?.fances?:0
            followuser?.fances =  fances +1
            followuser?.let { userRespository.save(it) }
            followRespository.save(follow1)
            logger.info("关注成功$user")
            BaseResult.SECUESS("关注成功",user)
        }

    }

    /**
     * 取消关注
     */
    override fun unFollowUser(body: ReqBody): BaseResult {
        if (body.followId == null) {
            return BaseResult.FAIL("要取消关注人的id不能为空")
        }
        val follow = body.userId?.let { followRespository.findByUserId(it) }
        val f = follow?.singleOrNull { it.followId == body.followId }
        return if (f != null) {
            val user = userRespository.findById(body.userId?:0)
            val followNum = user?.follows ?:0
            user?.follows = followNum -1
            user?.let { userRespository.save(it) }
            val followUser = userRespository.findById(body.followId?:0)
            val fanceNum =  followUser?.fances?:0
            followUser?.fances = fanceNum -1
            followUser?.let { userRespository.save(it) }
            followRespository.delete(f)
            logger.info("取消关注成功$user")
            BaseResult.SECUESS("取消关注成功",user)
        } else {
            BaseResult.FAIL("还未关注该用户")
        }
    }

    override fun getRecommend(body: ReqBody): BaseResult {
        val follow = body.userId?.let { followRespository.findByUserId(it) }
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val pages: Page<User> = userRespository.findAll(pageable)
        var list =  pages.filterNot {
            it.id == body.userId
        }
        val u =ArrayList<FollowVO>()
        if (follow.isNullOrEmpty()){
            list.map { it.isFollow = false
                val s = CopierUtil.copyProperties(it, FollowVO::class.java)
                s?.let { it1 -> u.add(it1) }
            }
            logger.info("获取关注列表$u")
            return BaseResult.SECUESS(u)
        }else{
          follow.forEach {
              list = list.filterNot { it1->
                  it1.id == it.followId
              }
          }
            list.map {

                val s = CopierUtil.copyProperties(it, FollowVO::class.java)
                s?.let { it1 -> u.add(it1) }

            }
            logger.info("获取关注列表$u")
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
        val user = userRespository.findById(body.userId ?: 0)
        return if (user != null) {
            user.fances = followRespository.findByFollowId(body.userId ?:0).size
            user.follows = followRespository.findByUserId(body.userId ?: 0).size
            userRespository.save(user)
            val s = CopierUtil.copyProperties(user, UserVO::class.java)
            BaseResult.SECUESS(s)
        }else{
            BaseResult.FAIL("该用户不存在")
        }
    }


}
