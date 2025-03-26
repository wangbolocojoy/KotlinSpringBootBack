package com.btm.back.controller

import com.btm.back.bean.PageBody
import com.btm.back.bean.ReqBody
import com.btm.back.dto.Article
import com.btm.back.imp.ArticleServiceImpl
import com.btm.back.utils.BaseResult
import com.btm.back.utils.PassToken
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * 文章控制器
 * @author Trae AI
 * @date 2023-06-01
 */
@Tag(name = "文章管理", description = "文章相关接口")
@RestController
@RequestMapping("/api/articles")
class ArticleController {

    @Autowired
    lateinit var articleService: ArticleServiceImpl
    
    /**
     * 创建文章
     * @param article 文章信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/create")
    fun createArticle(@RequestBody article: Article): BaseResult {
        return articleService.createArticle(article)
    }
    
    /**
     * 更新文章
     * @param article 文章信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/update")
    fun updateArticle(@RequestBody article: Article): BaseResult {
        return articleService.updateArticle(article)
    }
    
    /**
     * 删除文章
     * @param articleId 文章ID
     * @return 操作结果
     */
    @PassToken
    @DeleteMapping("/delete/{articleId}")
    fun deleteArticle(@PathVariable articleId: Int): BaseResult {
        return articleService.deleteArticle(articleId)
    }
    
    /**
     * 获取文章详情
     * @param articleId 文章ID
     * @return 文章详情
     */
    @PassToken
    @GetMapping("/detail/{articleId}")
    fun getArticleById(@PathVariable articleId: Int): BaseResult {
        return articleService.getArticleById(articleId)
    }
    
    /**
     * 分页获取所有文章
     * @param reqBody 请求参数
     * @return 文章列表
     */
    @PassToken
    @PostMapping("/list")
    fun getAllArticles(@RequestBody reqBody: ReqBody): BaseResult {
        val pageBody = PageBody(
            page = reqBody.page,
            pageSize = reqBody.pageSize,
            type = reqBody.type,
            novelName = null,
            userId = null,
            postState = null,
            postReport = null,
            postList = null,
            postId = null,
            public = null
        )
        return articleService.getAllArticles(pageBody)
    }
    
    /**
     * 分页获取所有文章 (GET方法)
     * @return 文章列表
     */
    @PassToken
    @GetMapping("/list")
    fun getAllArticlesGet(): BaseResult {
        val pageBody = PageBody(
            page = 1,
            pageSize = 10,
            type = null,
            novelName = null,
            userId = null,
            postState = null,
            postReport = null,
            postList = null,
            postId = null,
            public = null
        )
        return articleService.getAllArticles(pageBody)
    }
    
    /**
     * 根据作者获取文章列表
     * @param author 作者名称
     * @param reqBody 请求参数
     * @return 文章列表
     */
    @PassToken
    @PostMapping("/author/{author}")
    fun getArticlesByAuthor(
        @PathVariable author: String,
        @RequestBody reqBody: ReqBody
    ): BaseResult {
        val pageBody = PageBody(
            page = reqBody.page,
            pageSize = reqBody.pageSize,
            type = reqBody.type,
            novelName = null,
            userId = null,
            postState = null,
            postReport = null,
            postList = null,
            postId = null,
            public = null
        )
        return articleService.getArticlesByAuthor(author, pageBody)
    }
}