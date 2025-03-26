package com.btm.back.repository

import com.btm.back.dto.Lesson
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

/**
 * 课程章节数据访问接口
 * @author Trae AI
 * @date 2023-06-01
 */
interface LessonRepository : JpaRepository<Lesson, Int>, JpaSpecificationExecutor<Lesson> {
    /**
     * 根据课程ID查询章节列表
     * @param courseId 课程ID
     * @return 章节列表
     */
    fun findByCourseIdOrderByLessonOrderAsc(courseId: Int): List<Lesson>
    
    /**
     * 分页查询课程章节
     * @param courseId 课程ID
     * @param pageable 分页参数
     * @return 分页章节列表
     */
    fun findByCourseId(courseId: Int, pageable: Pageable): Page<Lesson>
}