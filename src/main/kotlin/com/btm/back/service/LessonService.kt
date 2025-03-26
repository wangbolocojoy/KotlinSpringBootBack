package com.btm.back.service

import com.btm.back.bean.PageBody
import com.btm.back.dto.Lesson
import com.btm.back.utils.BaseResult

/**
 * 课程章节服务接口
 * @author Trae AI
 * @date 2023-06-01
 */
interface LessonService {
    /**
     * 创建新章节
     * @param lesson 章节信息
     * @return 操作结果
     */
    fun createLesson(lesson: Lesson): BaseResult
    
    /**
     * 更新章节信息
     * @param lesson 章节信息
     * @return 操作结果
     */
    fun updateLesson(lesson: Lesson): BaseResult
    
    /**
     * 删除章节
     * @param lessonId 章节ID
     * @return 操作结果
     */
    fun deleteLesson(lessonId: Int): BaseResult
    
    /**
     * 获取章节详情
     * @param lessonId 章节ID
     * @return 章节详情
     */
    fun getLessonById(lessonId: Int): BaseResult
    
    /**
     * 获取课程的所有章节
     * @param courseId 课程ID
     * @return 章节列表
     */
    fun getLessonsByCourseId(courseId: Int): BaseResult
    
    /**
     * 分页获取课程章节
     * @param courseId 课程ID
     * @param pageBody 分页参数
     * @return 分页章节列表
     */
    fun getLessonsByCourseIdWithPage(courseId: Int, pageBody: PageBody): BaseResult
}