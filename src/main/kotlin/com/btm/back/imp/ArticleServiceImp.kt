package com.btm.back.imp

import com.btm.back.bean.ReqBody
import com.btm.back.dto.Article
import com.btm.back.repository.ArticleRespository
import com.btm.back.service.ArticleService
import com.btm.back.utils.BaseResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ArticleServiceImp : ArticleService {
    @Autowired
    lateinit var articleRespository: ArticleRespository
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    override fun creatArticle(body: ReqBody?): BaseResult {
        if (body == null) {
            return BaseResult.FAIL("参数不能为空")
        }
        val artice = Article()
        artice.article_Address_Id = body.article_Address_Id
        artice.article_Author = body.article_Author
        artice.article_AuthorId = body.article_AuthorId
        artice.article_Carry_Number = body.article_Carry_Number
        artice.article_Creattime = body.article_Creattime
        artice.article_Relase_Name = body.article_Relase_Name
        artice.article_State = body.article_State
        artice.article_Title = body.article_Title
        artice.article_Type = body.article_Type
        artice.article_Typename = body.article_Typename
        artice.article_Updatetime = body.article_Updatetime
        articleRespository.save(artice)
        return BaseResult.SECUESS(artice)
    }

    override fun getArticleDetailById(body: ReqBody?): BaseResult {
        val artice = body?.id?.let { articleRespository.findById(it) }
        return BaseResult.SECUESS(artice)
    }

    override fun getArticleByType(body: ReqBody?): BaseResult {
        val artice = body?.id?.let { articleRespository.findById(it) }
        return BaseResult.SECUESS(artice?.get()?.article_Type)
    }

    override fun getArticleList(body: ReqBody?): BaseResult {
        val pageable: Pageable = PageRequest.of(body?.page ?: 1, body?.pagesize ?: 10)
        val pages: Page<Article> = articleRespository.findAll(pageable)
        val iterator: MutableIterator<Article> = pages.iterator()
        return BaseResult.SECUESS(iterator)
//
    }

    override fun updateArticle(body: ReqBody?): BaseResult {
        val artice = body?.id?.let { articleRespository.findById(it) }
        if (artice ==null ){
            return BaseResult.FAIL("更新失败")
        }else{
            var article1 = body as?  Article
            article1?.let { articleRespository.save(it) }
            return  BaseResult.SECUESS(article1)
        }


    }

    override fun deleteArticleById(body: ReqBody?): BaseResult {
        val article = body?.id?.let { articleRespository.findById(it) }
        if (article!= null) {
            articleRespository.delete(article.get())
            return BaseResult.SECUESS("删除成功")
        } else {
            return BaseResult.FAIL("要删除的帖子找不到")
        }
    }

}
