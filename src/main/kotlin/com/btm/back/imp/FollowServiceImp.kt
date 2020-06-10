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
        val f = follow?.any { it.followid == body.followid }
        return if (f == true) {
            BaseResult.FAIL("已经关注该用户")
        } else {
            val follow1 = Follow()
            follow1.followid = body.followid
            follow1.userid = body.userid
            followRespository.save(follow1)
            BaseResult.SECUESS("关注成功")
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
            followRespository.delete(f)
            BaseResult.SECUESS("取消关注成功")
        } else {
            BaseResult.FAIL("还未关注该用户")
        }
    }

    override fun getRecommend(body: ReqBody): BaseResult {
        val follow = body.userid?.let { followRespository.findByFollowid(it) }
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pagesize ?: 10)
        val pages: Page<User> = userRespository.findAll(pageable)
        val iterator: MutableIterator<User> = pages.iterator()
        val users=ArrayList<FollowVO>()
        follow?.forEach { iterator.forEach { it1 ->
                if (it.userid != it1.id){
                    val s = CopierUtil.copyProperties(it1, FollowVO::class.java)
                    s?.let { it2 -> users.add(it2) }
            }
            }
        }
        if (users.isNullOrEmpty()){
            return  BaseResult.FAIL("推荐列表为空")
        }else{
            return  BaseResult.SECUESS(users)
        }

    }


}
