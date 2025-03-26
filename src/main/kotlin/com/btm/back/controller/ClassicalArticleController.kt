package com.btm.back.controller

import com.btm.back.bean.PageBody
import com.btm.back.bean.ReqBody
import com.btm.back.dto.ClassicalArticle
import com.btm.back.imp.ClassicalArticleServiceImpl
import com.btm.back.utils.BaseResult
import com.btm.back.utils.PassToken
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * 国学篇章控制器
 * @author Trae AI
 * @date 2023-06-01
 */
@Tag(name = "国学篇章管理", description = "国学篇章相关接口")
@RestController
@RequestMapping("/api/classical-articles")
class ClassicalArticleController {

    @Autowired
    lateinit var classicalArticleService: ClassicalArticleServiceImpl
    
    /**
     * 创建国学篇章
     * @param classicalArticle 国学篇章信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/create")
    fun createClassicalArticle(@RequestBody classicalArticle: ClassicalArticle): BaseResult {
        return classicalArticleService.createClassicalArticle(classicalArticle)
    }
    
    /**
     * 更新国学篇章
     * @param classicalArticle 国学篇章信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/update")
    fun updateClassicalArticle(@RequestBody classicalArticle: ClassicalArticle): BaseResult {
        return classicalArticleService.updateClassicalArticle(classicalArticle)
    }
    
    /**
     * 删除国学篇章
     * @param articleId 篇章ID
     * @return 操作结果
     */
    @PassToken
    @DeleteMapping("/delete/{articleId}")
    fun deleteClassicalArticle(@PathVariable articleId: Int): BaseResult {
        return classicalArticleService.deleteClassicalArticle(articleId)
    }
    
    /**
     * 获取国学篇章详情
     * @param articleId 篇章ID
     * @return 篇章详情
     */
    @PassToken
    @GetMapping("/detail/{articleId}")
    fun getClassicalArticleById(@PathVariable articleId: Int): BaseResult {
        return classicalArticleService.getClassicalArticleById(articleId)
    }
    
    /**
     * 分页获取所有国学篇章
     * @param reqBody 请求参数
     * @return 篇章列表
     */
    @PassToken
    @PostMapping("/list")
    fun getAllClassicalArticles(@RequestBody reqBody: ReqBody): BaseResult {
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
        return classicalArticleService.getAllClassicalArticles(pageBody)
    }
    
    /**
     * 分页获取所有国学篇章 (GET方法)
     * @return 篇章列表
     */
    @PassToken
    @GetMapping("/list")
    fun getAllClassicalArticlesGet(): BaseResult {
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
        return classicalArticleService.getAllClassicalArticles(pageBody)
    }
    
    /**
     * 根据作者获取国学篇章列表
     * @param author 作者名称
     * @param reqBody 请求参数
     * @return 篇章列表
     */
    @PassToken
    @PostMapping("/author/{author}")
    fun getClassicalArticlesByAuthor(
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
        return classicalArticleService.getClassicalArticlesByAuthor(author, pageBody)
    }
}