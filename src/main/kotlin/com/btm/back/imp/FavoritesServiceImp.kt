package com.btm.back.imp

import com.btm.back.bean.PostBody
import com.btm.back.bean.ReqBody
import com.btm.back.dto.Favorites
import com.btm.back.dto.PostStart
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.FavoritesRespository
import com.btm.back.repository.PostRespository
import com.btm.back.repository.PostStartRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.FavoritesService
import com.btm.back.utils.BaseResult
import com.btm.back.vo.PostVO
import com.btm.back.vo.UserVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class FavoritesServiceImp :FavoritesService{
    @Autowired
    lateinit var favoritesRespository: FavoritesRespository

    @Autowired
    lateinit var postRespository: PostRespository

    /**
    * 收藏
    * */
    override fun collection(body: PostBody): BaseResult {
        return if (body.userId != null && body.postId != null) {
            val p = favoritesRespository.findByPostId(body.postId ?: 0)
            if (p?.any { return@any it.userId == (body.userId ?: 0) } == true) {
                return BaseResult.SECUESS("已经收藏了")
            } else {
                val favorites = Favorites()
                favorites.postId = body.postId
                favorites.userId = body.userId
                favoritesRespository.save(favorites)
                BaseResult.SECUESS()
            }

        } else {
            BaseResult.FAIL("参数不能为空")
        }
    }
    /**
    * 取消收藏
    * */
    override fun cancelCollection(body: PostBody): BaseResult {
        val p = favoritesRespository.findByPostId(body.postId ?: 0)
        p?.forEach {
            if (it.userId == (body.userId ?: 0)) {
                favoritesRespository.delete(it)
                return@forEach
            }
        }
        return BaseResult.SECUESS("")
    }



   /**
   * @Description:  获取收藏列表
   * @Param:
   * @return:
   * @Author: hero
   * @Date: 2020-06-26
   **/
    override fun getCollectionList(body: PostBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val collectionList = favoritesRespository.findByUserId((body.userId ?: 0),pageable) ?: return BaseResult.FAIL("暂时没有收藏任何东西")
        val list=ArrayList<PostVO>()
        collectionList.forEach {
            val post = postRespository.findById(it.postId ?:0)
            if (post != null){
                val uvo = CopierUtil.copyProperties(it, PostVO::class.java)
                uvo?.let { it1 -> list.add(it1) }
            }
        }
        return BaseResult.SECUESS(list)
    }

}