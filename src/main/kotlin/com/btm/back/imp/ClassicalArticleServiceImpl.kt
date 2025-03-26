package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.dto.ClassicalArticle
import com.btm.back.repository.ClassicalArticleRepository
import com.btm.back.service.ClassicalArticleService
import com.btm.back.utils.BaseResult
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 国学篇章服务实现类
 * @author Trae AI
 * @date 2023-06-01
 */
@Service
@Transactional
class ClassicalArticleServiceImpl : ClassicalArticleService {
    private val logger = LoggerFactory.getLogger(ClassicalArticleServiceImpl::class.java)

    @Autowired
    lateinit var classicalArticleRepository: ClassicalArticleRepository

    /**
     * 创建新国学篇章
     * @param classicalArticle 国学篇章信息
     * @return 操作结果
     */
    override fun createClassicalArticle(classicalArticle: ClassicalArticle): BaseResult {
        try {
            val now = Date()
            classicalArticle.createdAt = now
            classicalArticle.updatedAt = now
            val savedArticle = classicalArticleRepository.save(classicalArticle)
            return BaseResult.SUCCESS("创建国学篇章成功", savedArticle)
        } catch (e: Exception) {
            logger.error("创建国学篇章失败: ${e.message}")
            return BaseResult.FAIL("创建国学篇章失败: ${e.message}")
        }
    }

    /**
     * 更新国学篇章信息
     * @param classicalArticle 国学篇章信息
     * @return 操作结果
     */
    override fun updateClassicalArticle(classicalArticle: ClassicalArticle): BaseResult {
        try {
            val articleId = classicalArticle.articleId
            if (articleId == null || !classicalArticleRepository.existsById(articleId)) {
                val now = Date()
                classicalArticle.createdAt = now
                classicalArticle.updatedAt = now
                val savedArticle = classicalArticleRepository.save(classicalArticle)
                return BaseResult.SUCCESS("创建国学篇章成功", savedArticle)
            }
            
            val existingArticle = classicalArticleRepository.findById(articleId).get()
            classicalArticle.createdAt = existingArticle.createdAt
            classicalArticle.updatedAt = Date()
            
            val updatedArticle = classicalArticleRepository.save(classicalArticle)
            return BaseResult.SUCCESS("更新国学篇章成功", updatedArticle)
        } catch (e: Exception) {
            logger.error("更新国学篇章失败: ${e.message}")
            return BaseResult.FAIL("更新国学篇章失败: ${e.message}")
        }
    }

    /**
     * 删除国学篇章
     * @param articleId 篇章ID
     * @return 操作结果
     */
    override fun deleteClassicalArticle(articleId: Int): BaseResult {
        try {
            if (!classicalArticleRepository.existsById(articleId)) {
                return BaseResult.FAIL("国学篇章不存在")
            }
            
            classicalArticleRepository.deleteById(articleId)
            return BaseResult.SUCCESS("删除国学篇章成功", null)
        } catch (e: Exception) {
            logger.error("删除国学篇章失败: ${e.message}")
            return BaseResult.FAIL("删除国学篇章失败: ${e.message}")
        }
    }

    /**
     * 获取国学篇章详情
     * @param articleId 篇章ID
     * @return 国学篇章详情
     */
    override fun getClassicalArticleById(articleId: Int): BaseResult {
        try {
            if (!classicalArticleRepository.existsById(articleId)) {
                return BaseResult.FAIL("国学篇章不存在")
            }
            
            val article = classicalArticleRepository.findById(articleId).get()
            return BaseResult.SUCCESS("获取国学篇章详情成功", article)
        } catch (e: Exception) {
            logger.error("获取国学篇章详情失败: ${e.message}")
            return BaseResult.FAIL("获取国学篇章详情失败: ${e.message}")
        }
    }

    /**
     * 分页获取所有国学篇章
     * @param pageBody 分页参数
     * @return 国学篇章列表
     */
   override fun getAllClassicalArticles(pageBody: PageBody): BaseResult {
    return try {
        // 使用 Sort.by("字段名").descending() 指定倒序排序
        val pageable = PageRequest.of(pageBody?.page ?:0, 10, Sort.by("createdAt").descending())
        val articles = classicalArticleRepository.findAll(pageable)
        val list = articles.content
        BaseResult.SUCCESS("获取国学篇章列表成功", list)
    } catch (e: Exception) {
        logger.error("获取国学篇章列表失败: ${e.message}")
        BaseResult.FAIL("获取国学篇章列表失败: ${e.message}")
    }
}


    /**
     * 根据作者获取国学篇章列表
     * @param author 作者名称
     * @param pageBody 分页参数
     * @return 国学篇章列表
     */
    override fun getClassicalArticlesByAuthor(author: String, pageBody: PageBody): BaseResult {
        return try {
            val pageable = PageRequest.of(0, 10)
            val articles = classicalArticleRepository.findByAuthor(author, pageable)
            BaseResult.SUCCESS("获取作者国学篇章列表成功", articles)
        } catch (e: Exception) {
            logger.error("获取作者国学篇章列表失败: ${e.message}")
            BaseResult.FAIL("获取作者国学篇章列表失败: ${e.message}")
        }
    }

    /**
     * 根据朝代获取国学篇章列表
     * @param dynasty 朝代
     * @param pageBody 分页参数
     * @return 国学篇章列表
     */
    override fun getClassicalArticlesByDynasty(dynasty: String, pageBody: PageBody): BaseResult {
        return try {
            val pageable = PageRequest.of(0, 10)

            val articles = classicalArticleRepository.findByDynasty(dynasty, pageable)
            BaseResult.SUCCESS("获取朝代国学篇章列表成功", articles)
        } catch (e: Exception) {
            logger.error("获取朝代国学篇章列表失败: ${e.message}")
            BaseResult.FAIL("获取朝代国学篇章列表失败: ${e.message}")
        }
    }
}