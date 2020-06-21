package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.bean.PostBody
import com.btm.back.dto.Post
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.PostRespository
import com.btm.back.repository.UserFilesRespository
import com.btm.back.repository.UserRespository
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

@Transactional
@Service
class PostServiceIml:PostService{
    @Autowired
    lateinit var postRespository: PostRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var userFilesRespository: UserFilesRespository
    private val logger: Logger = LoggerFactory.getLogger(PostServiceIml::class.java)
    override fun sendPost(body: PostBody): BaseResult {
        return if (body.userId != null) {
            val post = Post()
            post.userId = body.userId
            post.postTitle = body.postTitle
            post.postAddress = body.postAddress
            post.postDetail = body.postDetail
            post.postPublic = body.postPublic ?: false
            post.postStarts = body.postStart ?: 0
            postRespository.save(post)
            val user = userRespository.findById(body.userId ?:0)
            user?.postNum = user?.postNum?:0+1
            user?.let { userRespository.save(it) }
            val s = CopierUtil.copyProperties(post,PostVO::class.java)

            logger.info("发布帖子成功$s")
            BaseResult.SECUESS(s)
        } else {
            BaseResult.FAIL("用户id不能为空")
        }

    }

    override fun getPostByUserId(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val list = postRespository.findAllByUserId(body.userId ?:0,pageable)
        return if (list.isEmpty){
            BaseResult.FAIL("该用户暂时未发过帖子")
        }else{
            val images =ArrayList<PostVO>()
            list.forEach {
                val file = it.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                val listFvo = ArrayList<UserFilesVO>()
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
                s?.let { it1 -> images.add(it1) }
            }
            logger.info("获取用户帖子成功$images")
            BaseResult.SECUESS(images)
        }
    }

    override fun getPosts(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 3)
        val list = postRespository.findAll(pageable)
        return if (list.isEmpty){
            BaseResult.FAIL("暂时没有帖子")
        }else{
            val images =ArrayList<PostVO>()
            list.forEach {
                val file = it.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                val listFvo = ArrayList<UserFilesVO>()
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
                s?.let { it1 -> images.add(it1) }
            }
            logger.info("获取帖子成功$images")
            BaseResult.SECUESS(images)
        }
    }

    override fun deletePost(body: PageBody): BaseResult {
        val post = body.postId?.let { postRespository.findById(it) }
        return if (post != null&& body.userId != null){

            val list = userFilesRespository.findAllByPostId(body.postId ?:0)
            AliYunOssUtil.deleteFiles(body.userId.toString(),list)
            postRespository.delete(post)
            val user = userRespository.findById(body.userId ?: 0)
            if (user?.postNum?:0  >= 1){
                user?.postNum = user?.postNum?:0 - 1
            }else{
                user?.postNum = 0
            }
            user?.let { userRespository.save(it) }
            logger.info("删除帖子成功")
            BaseResult.SECUESS("删除成功")
        }else{
            BaseResult.FAIL("参数不能为空")
        }


    }
}
