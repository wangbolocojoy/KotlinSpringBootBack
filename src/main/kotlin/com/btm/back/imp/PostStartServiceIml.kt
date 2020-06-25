package com.btm.back.imp

import com.btm.back.bean.PostBody
import com.btm.back.dto.PostStart
import com.btm.back.repository.PostRespository
import com.btm.back.repository.PostStartRespository
import com.btm.back.service.PostStartService
import com.btm.back.utils.BaseResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PostStartServiceIml : PostStartService {

    @Autowired
    lateinit var postStartRespository: PostStartRespository

    @Autowired
    lateinit var postRespository: PostRespository

    override fun start(body: PostBody): BaseResult {

        return if (body.userId != null && body.postId != null) {
            val p = postStartRespository.findByPostId(body.postId ?: 0)
            val po = postRespository.findById(body.postId ?: 0) ?: return BaseResult.FAIL("帖子不存在")
            if (p?.any { return@any it.userId == (body.userId ?: 0) } == true) {

                return BaseResult.SECUESS("已经点赞了")
            } else {
                val postStart = PostStart()
                postStart.postId = body.postId
                postStart.userId = body.userId
                postStartRespository.save(postStart)
                po.postStarts = po.postStarts?.plus(1)
                postRespository.save(po)
                BaseResult.SECUESS()
            }

        } else {
            BaseResult.FAIL("参数不能为空")
        }

    }

    override fun unStart(body: PostBody): BaseResult {
        val p = postStartRespository.findByPostId(body.postId ?: 0)
        val po = postRespository.findById(body.postId ?:0) ?: return BaseResult.FAIL("帖子不存在")
        p?.forEach {
            if (it.userId == (body.userId ?: 0)) {
                postStartRespository.delete(it)
                return@forEach
            }
        }
        po.postStarts = po.postStarts?.minus(1)
        postRespository.save(po)
        return BaseResult.SECUESS()
    }

}

