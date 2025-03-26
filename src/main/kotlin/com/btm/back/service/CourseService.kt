package com.btm.back.service

import com.btm.back.bean.PageBody
import com.btm.back.dto.Course
import com.btm.back.utils.BaseResult

/**
 * 课程服务接口
 * @author Trae AI
 * @date 2023-06-01
 */
interface CourseService {
    /**
     * 创建新课程
     * @param course 课程信息
     * @return 操作结果
     */
    fun createCourse(course: Course): BaseResult
    
    /**
     * 更新课程信息
     * @param course 课程信息
     * @return 操作结果
     */
    fun updateCourse(course: Course): BaseResult
    
    /**
     * 删除课程
     * @param courseId 课程ID
     * @return 操作结果
     */
    fun deleteCourse(courseId: Int): BaseResult
    
    /**
     * 获取课程详情
     * @param courseId 课程ID
     * @return 课程详情
     */
    fun getCourseById(courseId: Int): BaseResult
    
    /**
     * 分页获取所有课程
     * @param pageBody 分页参数
     * @return 课程列表
     */
    fun getAllCourses(pageBody: PageBody): BaseResult

    fun getLatestCourses(page: Int): BaseResult
}