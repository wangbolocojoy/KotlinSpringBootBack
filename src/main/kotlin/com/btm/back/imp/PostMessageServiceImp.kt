package com.btm.back.imp

import com.btm.back.bean.MessageBody
import com.btm.back.bean.PageBody
import com.btm.back.dto.PostMessage
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.PostMessageRespository
import com.btm.back.repository.PostRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.PostMessageService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.MessageVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.collections.ArrayList

@Transactional
@Service
class PostMessageServiceImp:PostMessageService {

    @Autowired
    lateinit var postMessageRespository: PostMessageRespository

    @Autowired
    lateinit var postRespository: PostRespository

    @Autowired
    lateinit var userRespository: UserRespository

    private val logger: Logger = LoggerFactory.getLogger(PostMessageServiceImp::class.java)


    override fun getPostMessagesByPostId(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val msgList =  postMessageRespository.findByPostIdOrderByPostMsgCreatTimeDesc(body.postId ?:0,pageable) ?: return BaseResult.FAIL("暂时没有评论")
        val list = ArrayList<MessageVO>()

        msgList.forEach {
            val list1 = ArrayList<MessageVO>()
            val child  = postMessageRespository.findByPostIdOrderByPostMsgCreatTimeDesc(it.postMsgId?:0)
            val user = userRespository.findById(it.userId ?:0)

            child?.forEach { it1->
                val voo = CopierUtil.copyProperties(it1,MessageVO::class.java)
                val user1 = userRespository.findById(it1.userId ?:0)
                voo?.userIcon = user1?.icon
                voo?.userNickName = user1?.nickName
                voo?.messageStart = 0
                voo?.let { it2 -> list1.add(it2) }
            }
            val vo = CopierUtil.copyProperties(it,MessageVO::class.java)
            vo?.userIcon = user?.icon
            vo?.userNickName = user?.nickName
            vo?.messageStart = 0
            vo?.chiledMessage = list1
            vo?.let { it1 -> list.add(it1) }
            logger.info(vo.toString())
        }
        return  BaseResult.SECUESS(list)

    }

    override fun sendMessage(body: MessageBody): BaseResult {
        return if (body.postMessage.isNullOrEmpty()|| body.userId == null || body.postId == null){
            BaseResult.FAIL("评论信息不能为空")
        }else{
            val post = postRespository.findById(body.postId ?: 0)?:return BaseResult.FAIL()
            post.postMessageNum= post.postMessageNum?.plus(1)
            postRespository.save(post)
            val msg = PostMessage()
            msg.postId = body.postId
            msg.postMsgCreatTime = Date()
            msg.userId = body.userId
            msg.messageStart = 0
            msg.message = body.postMessage
            msg.postMsgId = body.postMsgId
            postMessageRespository.save(msg)
            BaseResult.SECUESS(msg)
        }
    }

    override fun deleteMessage(body: MessageBody): BaseResult {
        if ( body.userId == null || body.postId == null){
           return BaseResult.FAIL("参数不能为空")

        }else{
            val msg =postMessageRespository.findById(body.postMsgId?:0)
            return if (msg.isEmpty || msg.isPresent){
                BaseResult.FAIL("评论不存在")
            }else{
                if (body.userId == msg.get().userId){
                    val post = postRespository.findById(body.postId ?: 0)?:return BaseResult.FAIL()
                    post.postMessageNum= post.postMessageNum?.minus(1)
                    postRespository.save(post)
                    postMessageRespository.delete(msg.get())
                    BaseResult.SECUESS("删除成功")
                }else{
                    BaseResult.FAIL("不能删除别人的评论")
                }
            }

        }
    }
}
