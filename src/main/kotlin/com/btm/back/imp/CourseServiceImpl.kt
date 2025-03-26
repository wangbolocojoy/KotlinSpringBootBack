package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.dto.Course
import com.btm.back.repository.CourseRepository
import com.btm.back.service.CourseService
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
 * 课程服务实现类
 * @author Trae AI
 * @date 2023-06-01
 */
@Service
@Transactional
class CourseServiceImpl : CourseService {
    private val logger = LoggerFactory.getLogger(CourseServiceImpl::class.java)

    @Autowired
    lateinit var courseRepository: CourseRepository

    /**
     * 创建新课程
     * @param course 课程信息
     * @return 操作结果
     */
    override fun createCourse(course: Course): BaseResult {
        try {
            course.createdAt = Date()
            val savedCourse = courseRepository.save(course)
            return BaseResult.SUCCESS("创建课程成功", savedCourse)
        } catch (e: Exception) {
            logger.error("创建课程失败: ${e.message}")
            return BaseResult.FAIL("创建课程失败: ${e.message}")
        }
    }

    /**
     * 更新课程信息
     * @param course 课程信息
     * @return 操作结果
     */
    override fun updateCourse(course: Course): BaseResult {
        try {
            val existingCourse = courseRepository.findById(course.courseId!!).orElse(null)
                ?: return BaseResult.FAIL("课程不存在")

            existingCourse.courseName = course.courseName
            existingCourse.courseDescription = course.courseDescription

            val updatedCourse = courseRepository.save(existingCourse)
            return BaseResult.SUCCESS("更新课程成功", updatedCourse)
        } catch (e: Exception) {
            logger.error("更新课程失败: ${e.message}")
            return BaseResult.FAIL("更新课程失败: ${e.message}")
        }
    }

    /**
     * 删除课程
     * @param courseId 课程ID
     * @return 操作结果
     */
    override fun deleteCourse(courseId: Int): BaseResult {
        try {
            val existingCourse = courseRepository.findById(courseId).orElse(null)
                ?: return BaseResult.FAIL("课程不存在")

            courseRepository.delete(existingCourse)
            return BaseResult.SUCCESS("删除课程成功")
        } catch (e: Exception) {
            logger.error("删除课程失败: ${e.message}")
            return BaseResult.FAIL("删除课程失败: ${e.message}")
        }
    }

    /**
     * 获取课程详情
     * @param courseId 课程ID
     * @return 课程详情
     */
    override fun getCourseById(courseId: Int): BaseResult {
        try {
            val course = courseRepository.findById(courseId).orElse(null)
                ?: return BaseResult.FAIL("课程不存在")

            return BaseResult.SUCCESS("获取课程成功", course)
        } catch (e: Exception) {
            logger.error("获取课程失败: ${e.message}")
            return BaseResult.FAIL("获取课程失败: ${e.message}")
        }
    }

    /**
     * 分页获取所有课程
     * @param pageBody 分页参数
     * @return 课程列表
     */
    override fun getAllCourses(pageBody: PageBody): BaseResult {
        try {
            val pageable = PageRequest.of(
                pageBody.page ?: 0,
                pageBody.pageSize ?: 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
            )

            val coursePage = courseRepository.findAll(pageable)
            return BaseResult.SUCCESS(
                "获取课程列表成功",
                coursePage,

                )
        } catch (e: Exception) {
            logger.error("获取课程列表失败: ${e.message}")
            return BaseResult.FAIL("获取课程列表失败: ${e.message}")
        }
    }

    override fun getLatestCourses(page: Int): BaseResult {
        try {
            val pageable = PageRequest.of(
                0,
                 10,
                Sort.by(Sort.Direction.DESC, "createdAt")
            )

            val coursePage = courseRepository.findAll(pageable)
            return BaseResult.SUCCESS(
                "获取课程列表成功",
                coursePage,

                )
        } catch (e: Exception) {
            logger.error("获取课程列表失败: ${e.message}")
            return BaseResult.FAIL("获取课程列表失败: ${e.message}")
        }
    }
}