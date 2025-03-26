package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.Follow
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.BackInfoPlistRespository
import com.btm.back.repository.FollowRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.FollowService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.FollowVO
import com.btm.back.vo.UserVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
/**
 * 关注服务实现类
 * 负责用户关注相关的业务逻辑
 * @author Trae AI
 * @date 2023-06-01
 */
class FollowServiceImpl : FollowService {

    @Autowired
    lateinit var followRespository: FollowRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var backInfoPlistRespository: BackInfoPlistRespository
    private val logger: Logger = LoggerFactory.getLogger(FollowServiceImpl::class.java)

    /**
     * 获取关注列表
     * @param body 请求体，包含用户ID和分页信息
     * @return 关注用户列表
     */
    override fun getFollowList(body: ReqBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val backlist = body.userId?.let { backInfoPlistRespository.findByUserId(it) }
        val lis1 = backlist?.map { it.backId ?:0  }?.toMutableList()

        val follow = if (lis1.isNullOrEmpty()){
            body.userId?.let { followRespository.findByUserId(it,pageable) }
        }else{
            body.userId?.let { lis1.let { it1 -> followRespository.findByUserIdAndFollowIdNotIn(it, it1,pageable)} }
        }


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
            return BaseResult.SUCCESS(list)
        }
    }


    /**
     * 获取粉丝列表
     * @param body 请求体，包含用户ID和分页信息
     * @return 粉丝用户列表
     */
    override fun getFanceList(body: ReqBody): BaseResult {
       val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
       val backlist = body.userId?.let { backInfoPlistRespository.findByUserId(it) }
       val lis1 = backlist?.map { it.backId ?:0  }?.toMutableList()
       val fancelist = if (lis1.isNullOrEmpty()){
           body.userId?.let { followRespository.findByFollowId(it,pageable) }
       }else{
           body.userId?.let { lis1.let { it1 -> followRespository.findByFollowIdAndUserIdNotIn(it,it1,pageable) }}
       }
        return if (fancelist.isNullOrEmpty()) {
            BaseResult.FAIL("粉丝列表为空")
        } else {
            val list = ArrayList<FollowVO>()
            fancelist.forEach {
                val user = userRespository.findById(it.userId ?: 0)
                val followlist = body.userId?.let { followRespository.findByUserId(body.userId ?: 0,pageable) }
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
            BaseResult.SUCCESS(list)
        }
    }

    /**
     * 关注用户
     */
    override fun followUser(body: ReqBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        if (body.followId == null) {
            return BaseResult.FAIL("关注人id不能为空")
        }
        val follow = body.userId?.let { followRespository.findByUserId(it,pageable) }
        val user = userRespository.findById(body.userId?:0)
        val followuser = userRespository.findById(body.followId?:0)
        val f = follow?.any { it.followId == body.followId }
        return if (f == true) {
            BaseResult.FAIL("已经关注该用户")
        } else {
            val follow1 = Follow()
            follow1.followId = body.followId
            follow1.userId = body.userId
            val follows = user?.follows ?:0
            user?.follows = follows +1
            user?.let { userRespository.save(it) }
            val fances = followuser?.fances?:0

            followuser?.fances =  fances +1
            followuser?.let { userRespository.save(it) }
            followRespository.save(follow1)
            logger.info("关注成功$user")
            BaseResult.SUCCESS("关注成功",user)
        }
    }

    /**
     * 取消关注
     */
    override fun unFollowUser(body: ReqBody): BaseResult {
        if (body.followId == null) {
            return BaseResult.FAIL("要取消关注人的id不能为空")
        }
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val follow = body.userId?.let { followRespository.findByUserId(it,pageable) }
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
            BaseResult.SUCCESS("取消关注成功",user)
        } else {
            BaseResult.FAIL("还未关注该用户")
        }
    }

    /**
    * @Description: 获取推荐关注列表
    * @Param: 参数
    * @return: 返回数据
    * @Author: hero
    * @Date: 2020-08-05
    * @Time: 10:10
    **/
    override fun getRecommend(body: ReqBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val follow = body.userId?.let { followRespository.findAllByUserId(it) }
        val back = body.userId?.let { backInfoPlistRespository.findByUserId(it) }
        val  list1 : MutableList<Int>? = follow?.map { it.followId ?: 0 }?.toMutableList()
        val list2 : MutableList<Int>? = back?.map { it.backId ?: 0}?.toMutableList()
        list2?.let { list1?.addAll(it) }
        list1?.add(body.userId ?:0)
        val list = list1?.toList().let { it?.let { it1 -> userRespository.findByIdNotIn(it1,pageable) } } ?: userRespository.findAll(pageable)
        val volist = ArrayList<UserVO>()
        list.forEach {
            val u = CopierUtil.copyProperties(it,UserVO::class.java)
            u?.let { it1 -> volist.add(it1) }
        }
        return BaseResult.SUCCESS(volist)
    }

    override fun getuserfancesandfollows(body: ReqBody): BaseResult {
        val user = userRespository.findById(body.userId ?: 0)
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        return if (user != null) {
            user.fances = followRespository.findByFollowId(body.userId ?:0,pageable)?.size ?:0
            user.follows = followRespository.findByUserId(body.userId ?: 0,pageable)?.size ?:0
            userRespository.save(user)
            val s = CopierUtil.copyProperties(user, UserVO::class.java)
            BaseResult.SUCCESS(s)
        }else{
            BaseResult.FAIL("该用户不存在")
        }
    }


}
