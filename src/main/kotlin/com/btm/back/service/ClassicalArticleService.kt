package com.btm.back.service

import com.btm.back.bean.PageBody
import com.btm.back.dto.ClassicalArticle
import com.btm.back.utils.BaseResult

/**
 * 国学篇章服务接口
 * @author Trae AI
 * @date 2023-06-01
 */
interface ClassicalArticleService {
    /**
     * 创建新国学篇章
     * @param classicalArticle 国学篇章信息
     * @return 操作结果
     */
    fun createClassicalArticle(classicalArticle: ClassicalArticle): BaseResult
    
    /**
     * 更新国学篇章信息
     * @param classicalArticle 国学篇章信息
     * @return 操作结果
     */
    fun updateClassicalArticle(classicalArticle: ClassicalArticle): BaseResult
    
    /**
     * 删除国学篇章
     * @param articleId 篇章ID
     * @return 操作结果
     */
    fun deleteClassicalArticle(articleId: Int): BaseResult
    
    /**
     * 获取国学篇章详情
     * @param articleId 篇章ID
     * @return 国学篇章详情
     */
    fun getClassicalArticleById(articleId: Int): BaseResult
    
    /**
     * 分页获取所有国学篇章
     * @param pageBody 分页参数
     * @return 国学篇章列表
     */
    fun getAllClassicalArticles(pageBody: PageBody): BaseResult
    
    /**
     * 根据作者获取国学篇章列表
     * @param author 作者名称
     * @param pageBody 分页参数
     * @return 国学篇章列表
     */
    fun getClassicalArticlesByAuthor(author: String, pageBody: PageBody): BaseResult
    
    /**
     * 根据朝代获取国学篇章列表
     * @param dynasty 朝代
     * @param pageBody 分页参数
     * @return 国学篇章列表
     */
    fun getClassicalArticlesByDynasty(dynasty: String, pageBody: PageBody): BaseResult
}