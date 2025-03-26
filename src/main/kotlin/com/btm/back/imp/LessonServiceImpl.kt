package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.dto.Lesson
import com.btm.back.repository.CourseRepository
import com.btm.back.repository.LessonRepository
import com.btm.back.service.LessonService
import com.btm.back.utils.BaseResult
import com.btm.back.utils.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 课程章节服务实现类
 * @author Trae AI
 * @date 2023-06-01
 */
@Service
@Transactional
class LessonServiceImpl : LessonService {
    private val logger = LoggerFactory.getLogger(LessonServiceImpl::class.java)

    @Autowired
    lateinit var lessonRepository: LessonRepository

    @Autowired
    lateinit var courseRepository: CourseRepository

    /**
     * 创建新章节
     * @param lesson 章节信息
     * @return 操作结果
     */
    override fun createLesson(lesson: Lesson): BaseResult {
        try {
            // 验证课程是否存在
            val courseExists = courseRepository.existsById(lesson.courseId!!)
            if (!courseExists) {
                return BaseResult.FAIL("课程不存在")
            }

            lesson.createdAt = Date()
            val savedLesson = lessonRepository.save(lesson)
            return BaseResult.SUCCESS("创建章节成功", savedLesson)
        } catch (e: Exception) {
            logger.error("创建章节失败: ${e.message}")
            return BaseResult.FAIL("创建章节失败: ${e.message}")
        }
    }

    /**
     * 更新章节信息
     * @param lesson 章节信息
     * @return 操作结果
     */
    override fun updateLesson(lesson: Lesson): BaseResult {
        try {
            val existingLesson = lessonRepository.findById(lesson.lessonId!!).orElse(null)
                ?: return BaseResult.FAIL("章节不存在")

//            existingLesson.lessonName = lesson.lessonName
            existingLesson.lessonContent = lesson.lessonContent
//            existingLesson.lessonOrder = lesson.lessonOrder
            existingLesson.createdAt = Date()
            val updatedLesson = lessonRepository.save(existingLesson)
            return BaseResult.SUCCESS("更新章节成功", updatedLesson)
        } catch (e: Exception) {
            logger.error("更新章节失败: ${e.message}")
            return BaseResult.FAIL("更新章节失败: ${e.message}")
        }
    }

    /**
     * 删除章节
     * @param lessonId 章节ID
     * @return 操作结果
     */
    override fun deleteLesson(lessonId: Int): BaseResult{
        try {
            val existingLesson = lessonRepository.findById(lessonId).orElse(null)
                ?: return BaseResult.FAIL("章节不存在")

            lessonRepository.delete(existingLesson)
            return BaseResult.SUCCESS("删除章节成功")
        } catch (e: Exception) {
            logger.error("删除章节失败: ${e.message}")
            return BaseResult.FAIL("删除章节失败: ${e.message}")
        }
    }

    /**
     * 获取章节详情
     * @param lessonId 章节ID
     * @return 章节详情
     */
    override fun getLessonById(lessonId: Int): BaseResult{
        try {
            val lesson = lessonRepository.findById(lessonId).orElse(null)
                ?: return BaseResult.FAIL("章节不存在")

            return BaseResult.SUCCESS("获取章节成功", lesson)
        } catch (e: Exception) {
            logger.error("获取章节失败: ${e.message}")
            return BaseResult.FAIL("获取章节失败: ${e.message}")
        }
    }

    /**
     * 获取课程的所有章节
     * @param courseId 课程ID
     * @return 章节列表
     */
    override fun getLessonsByCourseId(courseId: Int): BaseResult{
        try {
            // 验证课程是否存在
            val courseExists = courseRepository.existsById(courseId)
            if (!courseExists) {
                return BaseResult.FAIL("课程不存在")
            }

            val lessons = lessonRepository.findByCourseIdOrderByLessonOrderAsc(courseId)
            if (lessons.isEmpty()) {
                return BaseResult.FAIL("该课程下没有章节")
            }
            return BaseResult.SUCCESS("获取章节列表成功", lessons)
        } catch (e: Exception) {
            logger.error("获取章节列表失败: ${e.message}")
            return BaseResult.FAIL("获取章节列表失败: ${e.message}")
        }
    }

    /**
     * 分页获取课程章节
     * @param courseId 课程ID
     * @param pageBody 分页参数
     * @return 分页章节列表
     */
    override fun getLessonsByCourseIdWithPage(courseId: Int, pageBody: PageBody): BaseResult {
        try {
            // 验证课程是否存在
            val courseExists = courseRepository.existsById(courseId)
            if (!courseExists) {
                return BaseResult.FAIL("课程不存在")
            }

            val pageable = PageRequest.of(
                pageBody.page ?: 0,
                pageBody.pageSize ?: 10,
                Sort.by(Sort.Direction.ASC, "lessonOrder")
            )

            val lessonPage = lessonRepository.findByCourseId(courseId, pageable)
            var lessons = lessonPage.content
            return BaseResult.SUCCESS(
                "获取章节列表成功",
                lessons)
        } catch (e: Exception) {
            logger.error("获取章节列表失败: ${e.message}")
            return BaseResult.FAIL("获取章节列表失败: ${e.message}")
        }
    }
}