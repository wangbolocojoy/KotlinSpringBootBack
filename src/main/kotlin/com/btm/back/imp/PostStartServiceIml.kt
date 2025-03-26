package com.btm.back.imp

import com.btm.back.bean.PostBody
import com.btm.back.dto.PostStart
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.PostRepository
import com.btm.back.repository.PostStartRepository
import com.btm.back.repository.UserRespository
import com.btm.back.service.PostStartService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.UserVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PostStartServiceIml : PostStartService {

    @Autowired
    lateinit var postStartRepository: PostStartRepository

    @Autowired
    lateinit var postRespository: PostRepository

    @Autowired
    lateinit var userRespository: UserRespository

    /**
    * @Description: 点赞
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:20
    **/
    override fun start(body: PostBody): BaseResult {

        return if (body.userId != null && body.postId != null) {
            val p = postStartRepository.findByPostId(body.postId ?: 0)
            val po = postRespository.findById(body.postId ?: 0) ?: return BaseResult.FAIL("帖子不存在")
            if (p?.any { return@any it.userId == (body.userId ?: 0) } == true) {

                return BaseResult.SUCCESS("已经点赞了")
            } else {
                val postStart = PostStart()
                postStart.postId = body.postId
                postStart.userId = body.userId
                postStartRepository.save(postStart)
                po.postStarts = po.postStarts?.plus(1)
                postRespository.save(po)
                BaseResult.SUCCESS()
            }

        } else {
            BaseResult.FAIL("参数不能为空")
        }

    }

    /**
    * @Description: 取消点赞
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:20
    **/
    override fun unStart(body: PostBody): BaseResult {
        val p = postStartRepository.findByPostId(body.postId ?: 0)
        val po = postRespository.findById(body.postId ?:0) ?: return BaseResult.FAIL("帖子不存在")
        p?.forEach {
            if (it.userId == (body.userId ?: 0)) {
                postStartRepository.delete(it)
                return@forEach
            }
        }
        po.postStarts = po.postStarts?.minus(1)
        postRespository.save(po)
        return BaseResult.SUCCESS()
    }

    /**
    * @Description: 获取点赞列表
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:25
    **/
    override fun getPostStartList(body: PostBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val startList = postStartRepository.findByPostId((body.postId ?: 0),pageable) ?: return BaseResult.FAIL("暂时没有点赞用户")
        val list=ArrayList<UserVO>()
        startList.forEach {
            val user = userRespository.findById(it.userId ?:0)
            if (user != null){
                val uvo = CopierUtil.copyProperties(user,UserVO::class.java)
                uvo?.let { it1 -> list.add(it1) }
            }
        }
        return BaseResult.SUCCESS(list)
    }

    override fun getUserStartList(body: PostBody): BaseResult {
        val usl = postStartRepository.findByUserId(body.userId ?:0) ?: return BaseResult.FAIL("你还没有给别人点过赞")
        return  BaseResult.SUCCESS(usl)
    }


}

