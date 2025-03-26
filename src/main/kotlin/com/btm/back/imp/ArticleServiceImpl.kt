package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.dto.Article
import com.btm.back.repository.ArticleRepository
import com.btm.back.service.ArticleService
import com.btm.back.utils.BaseResult
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 文章服务实现类
 * @author Trae AI
 * @date 2023-06-01
 */
@Service
@Transactional
class ArticleServiceImpl : ArticleService {
    private val logger = LoggerFactory.getLogger(ArticleServiceImpl::class.java)

    @Autowired
    lateinit var articleRepository: ArticleRepository

    /**
     * 创建新文章
     * @param article 文章信息
     * @return 操作结果
     */
    override fun createArticle(article: Article): BaseResult {
        try {
            val now = Date()
            article.createdAt = now
            article.updatedAt = now
            val savedArticle = articleRepository.save(article)
            return BaseResult.SUCCESS("创建文章成功", savedArticle)
        } catch (e: Exception) {
            logger.error("创建文章失败: ${e.message}")
            return BaseResult.FAIL("创建文章失败: ${e.message}")
        }
    }

    /**
     * 更新文章信息
     * @param article 文章信息
     * @return 操作结果
     */
    override fun updateArticle(article: Article): BaseResult {
        try {
            val existingArticle = articleRepository.findById(article.articleId!!).orElse(null)
                ?: return BaseResult.FAIL("文章不存在")

            existingArticle.title = article.title
            existingArticle.content = article.content
            existingArticle.images = article.images
            existingArticle.updatedAt = Date()

            val updatedArticle = articleRepository.save(existingArticle)
            return BaseResult.SUCCESS("更新文章成功", updatedArticle)
        } catch (e: Exception) {
            logger.error("更新文章失败: ${e.message}")
            return BaseResult.FAIL("更新文章失败: ${e.message}")
        }
    }

    /**
     * 删除文章
     * @param articleId 文章ID
     * @return 操作结果
     */
    override fun deleteArticle(articleId: Int): BaseResult {
        try {
            val existingArticle = articleRepository.findById(articleId).orElse(null)
                ?: return BaseResult.FAIL("文章不存在")

            articleRepository.delete(existingArticle)
            return BaseResult.SUCCESS("删除文章成功")
        } catch (e: Exception) {
            logger.error("删除文章失败: ${e.message}")
            return BaseResult.FAIL("删除文章失败: ${e.message}")
        }
    }

    /**
     * 获取文章详情
     * @param articleId 文章ID
     * @return 文章详情
     */
    override fun getArticleById(articleId: Int): BaseResult {
        try {
            val article = articleRepository.findById(articleId).orElse(null)
                ?: return BaseResult.FAIL("文章不存在")

            return BaseResult.SUCCESS("获取文章成功", article)
        } catch (e: Exception) {
            logger.error("获取文章失败: ${e.message}")
            return BaseResult.FAIL("获取文章失败: ${e.message}")
        }
    }

    /**
     * 分页获取所有文章
     * @param pageBody 分页参数
     * @return 文章列表
     */
    override fun getAllArticles(pageBody: PageBody): BaseResult {
        try {
            val pageable = PageRequest.of(
                pageBody.page ?: 0,
                pageBody.pageSize ?: 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
            )

            val articlePage = articleRepository.findAll(pageable)
            val articleList = articlePage.content
            return BaseResult.SUCCESS(
                "获取文章列表成功",
                articleList
            )
        } catch (e: Exception) {
            logger.error("获取文章列表失败: ${e.message}")
            return BaseResult.FAIL("获取文章列表失败: ${e.message}")
        }
    }

    /**
     * 根据作者获取文章列表
     * @param author 作者名称
     * @param pageBody 分页参数
     * @return 文章列表
     */
    override fun getArticlesByAuthor(author: String, pageBody: PageBody): BaseResult {
        try {
            val pageable = PageRequest.of(
                pageBody.page ?: 0,
                pageBody.pageSize ?: 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
            )

            val articlePage = articleRepository.findByAuthor(author, pageable)
            return BaseResult.SUCCESS(
                "获取作者文章列表成功",
                articlePage
            )
        } catch (e: Exception) {
            logger.error("获取作者文章列表失败: ${e.message}")
            return BaseResult.FAIL("获取作者文章列表失败: ${e.message}")
        }
    }

    override fun getLatestArticles(page: Int): BaseResult {
        try {
            val pageable = PageRequest.of(
                page - 1,  // 转换为Spring Data的页码（从0开始）
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")  // 显式指定排序字段
            )
            val articlePage = articleRepository.findAll(pageable)
            return BaseResult.SUCCESS("获取最新文章成功", articlePage)
        } catch (e: Exception) {
            logger.error("获取文章列表失败: ${e.message}")
            return BaseResult.FAIL("获取最新文章失败: ${e.message}") // 修正错误提示
        }
    }
}