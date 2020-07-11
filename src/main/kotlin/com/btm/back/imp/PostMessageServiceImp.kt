package com.btm.back.imp

import com.btm.back.bean.MessageBody
import com.btm.back.bean.PageBody
import com.btm.back.dto.MessageStart
import com.btm.back.dto.PostMessage
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.MessageStartRespository
import com.btm.back.repository.PostMessageRespository
import com.btm.back.repository.PostRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.PostMessageService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.MessageVO
import com.btm.back.vo.SendMsgVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.collections.ArrayList

@Transactional
@Service
//@CacheConfig(keyGenerator = "keyGenerator")
class PostMessageServiceImp : PostMessageService {

    @Autowired
    lateinit var postMessageRespository: PostMessageRespository

    @Autowired
    lateinit var postRespository: PostRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var messageStartRespository: MessageStartRespository

    private val logger: Logger = LoggerFactory.getLogger(PostMessageServiceImp::class.java)

    //    @Cacheable(cacheNames = ["getPostMessagesByPostId"])
    override fun getPostMessagesByPostId(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val msgList = postMessageRespository.findByPostIdOrderByPostMsgCreatTimeDesc(body.postId ?: 0, pageable)
                ?: return BaseResult.FAIL("暂时没有评论")
        val list = ArrayList<MessageVO>()

        msgList.forEach {
            val list1 = ArrayList<MessageVO>()
            val startList = it.id?.let { it4 -> messageStartRespository.findByMsgId(it4) }

            val child = postMessageRespository.findByPostMsgIdOrderByPostMsgCreatTimeAsc(it.id ?: 0)
            val user = userRespository.findById(it.userId ?: 0)
            child?.forEach { it1 ->
                val voo = CopierUtil.copyProperties(it1, MessageVO::class.java)

                val user1 = userRespository.findById(it1.userId ?: 0)
                voo?.userIcon = user1?.icon
                voo?.userNickName = user1?.nickName
                val startList1 = it1.id?.let { it2 -> messageStartRespository.findByMsgId(it2) }
                voo?.isStart = startList1?.any { it3->
                    it3.userId == body.userId
                }
                voo?.let { it2 -> list1.add(it2) }
            }
            val vo = CopierUtil.copyProperties(it, MessageVO::class.java)
            vo?.userIcon = user?.icon
            vo?.userNickName = user?.nickName
            vo?.isStart = startList?.any { it6->
                it6.userId == body.userId
            }
            vo?.chiledMessage = list1
            vo?.let { it1 -> list.add(it1) }
            logger.info(vo.toString())
        }
        return BaseResult.SECUESS(list)

    }

    //    @CacheEvict(cacheNames = ["getPosts","getPostByUserId","getPostMessagesByPostId"],allEntries = true)
    override fun sendMessage(body: MessageBody): BaseResult {
        return if (body.postMessage.isNullOrEmpty() || body.userId == null || body.postId == null) {
            BaseResult.FAIL("评论信息不能为空")
        } else {
            val post = postRespository.findById(body.postId ?: 0) ?: return BaseResult.FAIL()
            post.postMessageNum = post.postMessageNum?.plus(1)
            postRespository.save(post)

            val msg = PostMessage()
            if (body.postMsgId == null) {
                msg.postId = body.postId
            } else {
                msg.postId = null
            }
            msg.postMsgCreatTime = Date()
            msg.userId = body.userId
            msg.messageStart = 0
            msg.message = body.postMessage
            msg.replyNickName = body.replyNickName
            msg.postMsgId = body.postMsgId
            msg.replyUserId = body.replyUserId
            postMessageRespository.save(msg)
            BaseResult.SECUESS(msg)
        }
    }

    //    @CacheEvict(cacheNames = ["getPosts","getPostByUserId","getPostMessagesByPostId"],allEntries = true)
    override fun deleteMessage(body: MessageBody): BaseResult {
        return if (body.userId == null || body.id == null || body.postId == null) {
            BaseResult.FAIL("参数不能为空")
        } else {

            val msg = postMessageRespository.findById(body.id ?: 0)
            if (msg != null && msg.userId == body.userId) {
                val post = postRespository.findById(body.postId ?: 0)
                post?.postMessageNum = post?.postMessageNum?.minus(1)
                post?.let { postRespository.save(it) }
                val user = userRespository.findById(body.userId ?: 0)
                postMessageRespository.delete(msg)
                BaseResult.SECUESS()
            } else {
                BaseResult.FAIL("删除失败")
            }

        }
    }

    override fun getMyMassages(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val msgList = postMessageRespository.findByUserIdOrderByPostMsgCreatTimeDesc(body.userId ?: 0, pageable)
                ?: return BaseResult.FAIL("暂时没有评论")
        val list = ArrayList<MessageVO>()
        val user = userRespository.findById(body.userId ?: 0)
        msgList.forEach {
            val vo = CopierUtil.copyProperties(it, MessageVO::class.java)
            vo?.userIcon = user?.icon
            vo?.userNickName = user?.nickName
            vo?.messageStart = 0
            vo?.let { it1 -> list.add(it1) }
        }
        return BaseResult.SECUESS(list)
    }

    override fun startMassage(body: MessageBody): BaseResult {
        if (body.userId == null || body.msgId == null) {
            return BaseResult.FAIL("参数不足")
        } else {
            val msg = postMessageRespository.findById(body.msgId ?: 0) ?: return BaseResult.FAIL("评论不存在")
            val msgList = messageStartRespository.findByMsgId(body.msgId ?: 0)
            if (msgList?.any { return@any it.userId == (body.userId ?: 0) } == true) {
                return BaseResult.SECUESS("已经点赞了")
            } else {
                val mStart = MessageStart()
                mStart.userId = body.userId
                mStart.msgId = body.msgId
                messageStartRespository.save(mStart)
                msg.messageStart = msg.messageStart?.plus(1)
                postMessageRespository.save(msg)
                return BaseResult.SECUESS(msg)
            }

        }
    }

    override fun unStartMassage(body: MessageBody): BaseResult {
        if (body.userId == null || body.msgId == null) {
            return BaseResult.FAIL("参数不足")
        } else {
            val p = messageStartRespository.findByMsgId(body.msgId ?: 0)
            val msg = postMessageRespository.findById(body.msgId ?: 0) ?: return BaseResult.FAIL("评论不存在")
            p?.forEach {
                if (it.userId == (body.userId ?: 0)) {
                    messageStartRespository.delete(it)
                    return@forEach
                }
            }
            msg.messageStart = msg.messageStart?.minus(1)
            postMessageRespository.save(msg)
            return BaseResult.SECUESS()
        }

    }
}
