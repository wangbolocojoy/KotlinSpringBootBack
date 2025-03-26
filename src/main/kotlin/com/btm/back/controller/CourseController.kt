package com.btm.back.controller

import com.btm.back.bean.PageBody
import com.btm.back.bean.ReqBody
import com.btm.back.dto.Course
import com.btm.back.imp.CourseServiceImpl
import com.btm.back.utils.BaseResult
import com.btm.back.utils.PassToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * 课程控制器
 * @author Trae AI
 * @date 2023-06-01
 */
@RestController
@RequestMapping("/api/courses")
class CourseController {

    @Autowired
    lateinit var courseService: CourseServiceImpl
    
    /**
     * 创建课程
     * @param course 课程信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/create")
    fun createCourse(@RequestBody course: Course): BaseResult {
        return courseService.createCourse(course)
    }
    
    /**
     * 更新课程
     * @param course 课程信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/update")
    fun updateCourse(@RequestBody course: Course): BaseResult {
        return courseService.updateCourse(course)
    }
    
    /**
     * 删除课程
     * @param courseId 课程ID
     * @return 操作结果
     */
    @PassToken
    @DeleteMapping("/delete/{courseId}")
    fun deleteCourse(@PathVariable courseId: Int): BaseResult {
        return courseService.deleteCourse(courseId)
    }
    
    /**
     * 获取课程详情
     * @param courseId 课程ID
     * @return 课程详情
     */
    @PassToken
    @GetMapping("/detail/{courseId}")
    fun getCourseById(@PathVariable courseId: Int): BaseResult {
        return courseService.getCourseById(courseId)
    }
    
    /**
     * 分页获取所有课程
     * @param reqBody 请求参数
     * @return 课程列表
     */
    @PassToken
    @PostMapping("/list")
    fun getAllCourses(@RequestBody reqBody: ReqBody): BaseResult {
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
        return courseService.getAllCourses(pageBody)
    }
}