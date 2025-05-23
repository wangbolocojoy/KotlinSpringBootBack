package com.btm.back.imp

import com.btm.back.bean.PostBody
import com.btm.back.dto.Favorites
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.*
import com.btm.back.service.FavoritesService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.PostAuthorVo
import com.btm.back.vo.PostVO
import com.btm.back.vo.UserFilesVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
/**
 * 收藏服务实现类
 * 负责帖子收藏相关的业务逻辑
 * @author Trae AI
 * @date 2023-06-01
 */
class FavoritesServiceImpl : FavoritesService {
    @Autowired
    lateinit var favoritesRespository: FavoritesRespository

    @Autowired
    lateinit var postRespository: PostRepository

    @Autowired
    lateinit var userFilesRespository: UserFilesRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var postStartRepository: PostStartRepository

    private val logger: Logger = LoggerFactory.getLogger(FavoritesServiceImpl::class.java)

    /**
     * 收藏帖子
     * @param body 包含用户ID和帖子ID的请求体
     * @return 操作结果
     */
    @CacheEvict(cacheNames = ["getPosts","getPostByUserId"],allEntries = true)
    override fun collection(body: PostBody): BaseResult {
        return if (body.userId != null && body.postId != null) {
            val p = favoritesRespository.findByPostId(body.postId ?: 0)
            if (p?.any { return@any it.userId == (body.userId ?: 0) } == true) {
                return BaseResult.SUCCESS("已经收藏了")
            } else {
                val favorites = Favorites()
                favorites.postId = body.postId
                favorites.userId = body.userId
                favoritesRespository.save(favorites)
                BaseResult.SUCCESS()
            }

        } else {
            BaseResult.FAIL("参数不能为空")
        }
    }
    /**
     * 取消收藏
     * @param body 包含用户ID和帖子ID的请求体
     * @return 操作结果
     */
    @CacheEvict(cacheNames = ["getPosts","getPostByUserId"],allEntries = true)
    override fun cancelCollection(body: PostBody): BaseResult {
        val p = favoritesRespository.findByPostId(body.postId ?: 0)
        p?.forEach {
            if (it.userId == (body.userId ?: 0)) {
                favoritesRespository.delete(it)
                return@forEach
            }
        }
        return BaseResult.SUCCESS("")
    }



    /**
     * 获取收藏列表
     * @param body 包含用户ID和分页信息的请求体
     * @return 收藏的帖子列表
     */

    override fun getCollectionList(body: PostBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 5)
        val collectionList = favoritesRespository.findByUserId((body.userId ?: 0),pageable) ?: return BaseResult.FAIL("暂时没有收藏任何东西")
        val list=ArrayList<PostVO>()
        collectionList.forEach {
            val post = postRespository.findById(it.postId ?:0)
            if (post != null){
                val startList = postStartRepository.findByUserId(body.userId?:0)
                val pvo = CopierUtil.copyProperties(post, PostVO::class.java)
                logger.info(pvo.toString())
                val user = userRespository.findById(post.userId?:0)
                if (user != null){
                    pvo?.author = CopierUtil.copyProperties(user,PostAuthorVo::class.java)
                }

                val images = ArrayList<UserFilesVO>()
                val file = post.id?.let { it1 -> userFilesRespository.findAllByPostId(it1) }
                file?.map { it3 ->
                    val fvo = CopierUtil.copyProperties(it3,UserFilesVO::class.java)
                    fvo?.let { it1 -> images.add(it1) }
                }
                pvo?.isStart = startList?.any { it4->
                    it4.postId == post.id
                }
                pvo?.isCollection = true
                pvo?.postImages = images
                pvo?.let { it1 -> list.add(it1) }
            }
        }
        return BaseResult.SUCCESS(list)
    }

}
