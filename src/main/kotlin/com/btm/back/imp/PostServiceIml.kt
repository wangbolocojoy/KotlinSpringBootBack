package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.bean.PostBody
import com.btm.back.dto.Post
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.PostRespository
import com.btm.back.repository.UserFilesRespository
import com.btm.back.service.PostService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import com.btm.back.vo.PostVO
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
    lateinit var userFilesRespository: UserFilesRespository

    override fun sendPost(body: PostBody): BaseResult {
        return if (body.userid != null) {
            val post = Post()
            post.userId = body.userid
            post.posttitle = body.posttitle
            post.postaddress = body.postaddress
            post.postdetail = body.postdetail
            post.postpublic = body.postpulic ?: false
            post.poststarts = body.poststart ?: 0
            postRespository.save(post)
            val s = CopierUtil.copyProperties(post,PostVO::class.java)
            BaseResult.SECUESS(s)
        } else {
            BaseResult.FAIL("用户id不能为空")
        }

    }

    override fun getPostByUserId(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 1, body.pagesize ?: 10)
        val list = postRespository.findAllByUserId(body.userid ?:0,pageable)
        return if (list.isEmpty){
            BaseResult.FAIL("该用户暂时未发过帖子",list)
        }else{
            val images =ArrayList<PostVO>()
            list.forEach {
                val file = it.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                val s =CopierUtil.copyProperties(it,PostVO::class.java)
                s?.postImages = file
                s?.let { it1 -> images.add(it1) }
            }
            BaseResult.SECUESS(images)
        }
    }

    override fun getPosts(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 1, body.pagesize ?: 10)
        val list = postRespository.findAll(pageable)
        return if (list.isEmpty){
            BaseResult.FAIL("暂时没有帖子")
        }else{
            BaseResult.SECUESS(list)
        }
    }

    override fun deletePost(body: PageBody): BaseResult {
        val post = body.postid?.let { postRespository.findById(it) }
        return if (post != null){
            val list = userFilesRespository.findAllByPostId(body.postid ?:0)
            AliYunOssUtil.deleteFiles(body.userid.toString(),list)
            postRespository.delete(post)
            BaseResult.SECUESS("删除成功")
        }else{
            BaseResult.FAIL("帖子id不能为空")
        }


    }
}
