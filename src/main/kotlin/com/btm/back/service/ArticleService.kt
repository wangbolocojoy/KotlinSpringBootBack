package com.btm.back.service

import com.btm.back.bean.PageBody
import com.btm.back.dto.Article
import com.btm.back.utils.BaseResult

/**
 * 文章服务接口
 * @author Trae AI
 * @date 2023-06-01
 */
interface ArticleService {
    /**
     * 创建新文章
     * @param article 文章信息
     * @return 操作结果
     */
    fun createArticle(article: Article): BaseResult
    
    /**
     * 更新文章信息
     * @param article 文章信息
     * @return 操作结果
     */
    fun updateArticle(article: Article): BaseResult
    
    /**
     * 删除文章
     * @param articleId 文章ID
     * @return 操作结果
     */
    fun deleteArticle(articleId: Int): BaseResult
    
    /**
     * 获取文章详情
     * @param articleId 文章ID
     * @return 文章详情
     */
    fun getArticleById(articleId: Int): BaseResult
    
    /**
     * 分页获取所有文章
     * @param pageBody 分页参数
     * @return 文章列表
     */
    fun getAllArticles(pageBody: PageBody): BaseResult
    
    /**
     * 根据作者获取文章列表
     * @param author 作者名称
     * @param pageBody 分页参数
     * @return 文章列表
     */
    fun getArticlesByAuthor(author: String, pageBody: PageBody): BaseResult
    fun getLatestArticles(page:Int): BaseResult
}