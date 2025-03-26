package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.dto.Photography
import com.btm.back.repository.PhotographyRepository
import com.btm.back.service.PhotographyService
import com.btm.back.utils.BaseResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PhotographyServiceImpl : PhotographyService {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var photographyRepository: PhotographyRepository

    override fun photography() {
        // 临时占位方法，根据实际需求实现
    }

    override fun getAllPhotography(pageBody: PageBody): BaseResult {
        try {
            val pageable = PageRequest.of(
                pageBody.page ?: 0,
                pageBody.pageSize ?: 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
            )
            val pageResult = photographyRepository.findAll(pageable)
            return BaseResult.SUCCESS("获取摄影作品列表成功", pageResult)
        } catch (e: Exception) {
            logger.error("获取摄影作品列表失败: ${e.message}")
            return BaseResult.FAIL("获取摄影作品列表失败: ${e.message}")
        }
    }

    override fun getPhotographyByAuthor(author: String, pageBody: PageBody): BaseResult {
        try {
            val pageable = PageRequest.of(
                pageBody.page ?: 0,
                pageBody.pageSize ?: 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
            )
            val pageResult = photographyRepository.findByAuthor(author, pageable)
            return BaseResult.SUCCESS("获取作者摄影作品成功", pageResult)
        } catch (e: Exception) {
            logger.error("获取作者摄影作品失败: ${e.message}")
            return BaseResult.FAIL("获取作者摄影作品失败: ${e.message}")
        }
    }

    override fun getLatestPhotographs(page: Int): BaseResult {
        try {
            val pageable = PageRequest.of(
                page - 1,  // 转换为Spring Data的页码（从0开始）
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")  // 显式指定排序字段
            )

            val articlePage = photographyRepository.findAll(pageable)
            return BaseResult.SUCCESS(
                "获取最新摄影作品成功",
                articlePage
            )
        } catch (e: Exception) {
            logger.error("获取最新摄影作品失败: ${e.message}")
            return BaseResult.FAIL("获取最新摄影作品失败: ${e.message}")
        }
    }

    override fun getPhotographyById(photographyId: Int): BaseResult {
        try {
            val photography = photographyRepository.findById(photographyId).orElse(null)
                ?: return BaseResult.FAIL("摄影作品不存在")
            return BaseResult.SUCCESS("获取摄影作品成功", photography)
        } catch (e: Exception) {
            logger.error("获取摄影作品失败: ${e.message}")
            return BaseResult.FAIL("获取摄影作品失败: ${e.message}")
        }
    }

    override fun updatePhotography(photography: Photography): BaseResult {
        try {
            val existing = photographyRepository.findById(photography.photographyId!!).orElse(null)
                ?: return BaseResult.FAIL("摄影作品不存在")
            existing.title = photography.title
            existing.content = photography.content
            existing.updatedAt = Date()
            val updated = photographyRepository.save(existing)
            return BaseResult.SUCCESS("更新摄影作品成功", updated)
        } catch (e: Exception) {
            logger.error("更新摄影作品失败: ${e.message}")
            return BaseResult.FAIL("更新摄影作品失败: ${e.message}")
        }
    }

    override fun deletePhotography(photographyId: Int): BaseResult {
        try {
            val existing = photographyRepository.findById(photographyId).orElse(null)
                ?: return BaseResult.FAIL("摄影作品不存在")
            photographyRepository.delete(existing)
            return BaseResult.SUCCESS("删除摄影作品成功")
        } catch (e: Exception) {
            logger.error("删除摄影作品失败: ${e.message}")
            return BaseResult.FAIL("删除摄影作品失败: ${e.message}")
        }
    }

    override fun createPhotography(photography: Photography): BaseResult {
        try {
            photography.createdAt = Date() // 显式设置创建时间
            photography.updatedAt = Date() // 新增更新时间字段
            val saved = photographyRepository.save(photography)
            return BaseResult.SUCCESS("创建摄影作品成功", saved)
        } catch (e: Exception) {
            logger.error("创建摄影作品失败: ${e.message}")
            return BaseResult.FAIL("创建摄影作品失败: ${e.message}")
        }
    }
}