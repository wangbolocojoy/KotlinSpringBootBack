package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.bean.PostBody
import com.btm.back.dto.Post
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.*
import com.btm.back.service.PostService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import com.btm.back.vo.PostAuthorVo
import com.btm.back.vo.PostVO
import com.btm.back.vo.UserFilesVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Transactional
@Service
class PostServiceIml:PostService{
    @Autowired
    lateinit var postRespository: PostRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var postStartRespository: PostStartRespository

    @Autowired
    lateinit var userFilesRespository: UserFilesRespository

    @Autowired
    lateinit var favoritesRespository: FavoritesRespository

    @Autowired
    lateinit var postMessageRespository: PostMessageRespository

    private val logger: Logger = LoggerFactory.getLogger(PostServiceIml::class.java)

    /**
    * @Description: 发帖
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:18
    **/
    override fun sendPost(body: PostBody): BaseResult {
        return if (body.userId != null) {
            val post = Post()
            post.userId = body.userId
            post.postAddress = body.postAddress
            post.postDetail = body.postDetail
            post.postPublic = body.postPublic ?: false
            post.postStarts = body.postStart ?: 0
            val smp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            post.creatTime = Date()
            post.postMessageNum = 0
            postRespository.save(post)

            val user = userRespository.findById(body.userId ?:0)
            user?.postNum =(user?.postNum?:0)+1
            user?.let { userRespository.save(it) }
            val s = CopierUtil.copyProperties(post,PostVO::class.java)

            logger.info("发布帖子成功$s")
            BaseResult.SECUESS(s)
        } else {
            BaseResult.FAIL("用户id不能为空")
        }

    }

    /**
    * @Description: 获取用户的帖子
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:18
    **/
    override fun getPostByUserId(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val list = postRespository.findByUserIdOrderByCreatTimeDesc(body.userId ?:0,pageable)
        return if (list?.isEmpty == true){
            BaseResult.SECUESS("该用户暂时未发过帖子")
        }else{
            val images =ArrayList<PostVO>()
            val startList = postStartRespository.findByUserId(body.userId?:0)
            val collList = favoritesRespository.findByUserId(body.userId?:0)
            list?.forEach {


                val file = it.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                val listFvo = ArrayList<UserFilesVO>()
                val poststartnum = postStartRespository.findByPostId(it.id?:0)
                val msgList = postMessageRespository.findByPostId(body.postId ?:0)
                val user =  userRespository.findById(it.userId ?: 0)
                var postAuth:PostAuthorVo? = null
                if (user!= null){
                    postAuth = CopierUtil.copyProperties(user,PostAuthorVo::class.java)
                }
                file?.map {it2 ->
                    val s = CopierUtil.copyProperties(it2,UserFilesVO::class.java)
                    s?.let { it3 -> listFvo.add(it3) }
                }
                val s =CopierUtil.copyProperties(it,PostVO::class.java)
                s?.postImages = listFvo
                s?.author = postAuth
                s?.msgNum = msgList?.size
                s?.postStarts = poststartnum?.size ?: 0
                s?.isStart = startList?.any { it5->
                     it5.postId == it.id
                }
                s?.isCollection = collList?.any { it6->
                    it6.postId == it.id
                }
                s?.creatTime = it.creatTime
                s?.let { it1 -> images.add(it1) }
            }
            logger.info("获取用户帖子成功$images")
            BaseResult.SECUESS(images)
        }
    }

    /**
    * @Description: 分页获取所有帖子
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:19
    **/
    override fun getPosts(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 3)
        val list = postRespository.findByOrderByCreatTimeDesc(pageable)
        return if (list?.isEmpty == true){
            BaseResult.SECUESS("暂时没有帖子")
        }else{
            val startList = postStartRespository.findByUserId(body.userId?:0)
            val collList = favoritesRespository.findByUserId(body.userId?:0)
            val images =ArrayList<PostVO>()
            list?.forEach {
                val file = it.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                val listFvo = ArrayList<UserFilesVO>()
                val msgList = postMessageRespository.findByPostId(body.postId ?:0)
                val user =  userRespository.findById(it.userId ?: 0)
                var postAuth : PostAuthorVo? = null
                if (user!= null ){
                    postAuth = CopierUtil.copyProperties(user,PostAuthorVo::class.java)
                }
                 file?.map {it2 ->
                     val s = CopierUtil.copyProperties(it2,UserFilesVO::class.java)
                     s?.let { it3 -> listFvo.add(it3) }
                }
                val s =CopierUtil.copyProperties(it,PostVO::class.java)
                s?.author = postAuth
                s?.postImages = listFvo
                s?.msgNum = msgList?.size
                s?.isStart = startList?.any { it5->
                    it5.postId == it.id
                }
                s?.isCollection = collList?.any { it6->
                    it6.postId == it.id
                }
                s?.creatTime = it.creatTime
                s?.let { it1 -> images.add(it1) }
            }
            logger.info("获取帖子成功$images")
            BaseResult.SECUESS(images)
        }
    }

    /**
    * @Description: 用户删除帖子
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:19
    **/
    override fun deletePost(body: PageBody): BaseResult {
        val post = body.postId?.let { postRespository.findById(it) }
        return if (post != null&& body.userId != null){

            val list = userFilesRespository.findAllByPostId(body.postId ?:0)
            AliYunOssUtil.deleteFiles(body.userId.toString(),list)
            postRespository.delete(post)
            val user = userRespository.findById(body.userId ?: 0)
            if (user?.postNum?:0  >= 1){
                user?.postNum = (user?.postNum?:0) - 1
            }else{
                user?.postNum = 0
            }
            user?.let { userRespository.save(it) }
            logger.info("删除帖子成功")
            BaseResult.SECUESS("删除成功")
        }else{
            BaseResult.FAIL("该帖子不存在")
        }


    }

    /**
    * @Description: 更新帖子点赞数量
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:19
    **/
    override fun updatePostLikeStartt(body: PageBody): BaseResult {
        val user = body.userId?.let { userRespository.findById(it) }
        if (user != null){
              val users = body.postId?.let { postStartRespository.findByPostId(it) }
              val has = users?.any { it.id == body.userId }

        return BaseResult.FAIL()

        }else{
            return  BaseResult.FAIL("用户不存在")
        }
    }
}
