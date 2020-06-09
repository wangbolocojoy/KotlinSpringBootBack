package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.Follow
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.FollowRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.FollowService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.UserVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FollowServiceImp :FollowService{

    @Autowired
    lateinit var followRespository: FollowRespository
    lateinit var userRespository: UserRespository
    /**
     * 获取关注列表
     */
    override fun getFollowList(body: ReqBody): BaseResult {
        val follow = body.id?.let { followRespository.findByUserid(it) }
        if (follow.isNullOrEmpty()){
            return  BaseResult.FAIL("关注列表为空")
        }else{
            val list : List<UserVo>? = null
            follow.forEach {
                  val user = userRespository.findById(it.followid?:0)
                   if (user != null){
                       val s = CopierUtil.copyProperties(user, UserVo::class.java)
                       list?.plus(s)
                   }
            }
            return  BaseResult.SECUESS(list)
        }
    }
    /**
     * 获取粉丝列表
     */
    override fun getFanceList(body: ReqBody): BaseResult {
        val f = body.id?.let { followRespository.findByFollowid(it) }
        return if (f.isNullOrEmpty()){
            BaseResult.FAIL("粉丝列表为空")
        }else{
            val list : List<UserVo>? = null
            f.forEach {
                    val user = userRespository.findById(it.userid?:0)
                if (user != null) {
                    val s = CopierUtil.copyProperties(user, UserVo::class.java)
                    list?.plus(s)
                }
            }
            BaseResult.SECUESS(list)
        }
    }

    /**
     * 关注用户
     */
    override fun followUser(body: ReqBody): BaseResult {
        val follow = body.userid?.let { followRespository.findByUserid(it) }
        val f = follow?.any { it.followid == body.followid }
        return if (f == true){
            BaseResult.FAIL("已经关注该用户")
        }else{
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
        val follow = body.userid?.let { followRespository.findByUserid(it) }
        val f = follow?.singleOrNull{it.followid == body.followid}
        return if (f != null){
            followRespository.delete(f)
            BaseResult.SECUESS("取消关注成功")
        }else{
            BaseResult.FAIL("还未关注该用户")
        }
    }



}