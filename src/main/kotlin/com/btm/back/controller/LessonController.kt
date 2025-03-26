package com.btm.back.controller

import com.btm.back.bean.PageBody
import com.btm.back.bean.ReqBody
import com.btm.back.dto.Lesson
import com.btm.back.imp.LessonServiceImpl
import com.btm.back.utils.BaseResult
import com.btm.back.utils.PassToken
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * 课程章节控制器
 * @author Trae AI
 * @date 2023-06-01
 */
@Tag(name = "课程章节管理", description = "课程章节相关接口")
@RestController
@RequestMapping("/api/lessons")
class LessonController {

    @Autowired
    lateinit var lessonService: LessonServiceImpl
    
    /**
     * 创建章节
     * @param lesson 章节信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/create")
    fun createLesson(@RequestBody lesson: Lesson): BaseResult {
        return lessonService.createLesson(lesson)
    }
    
    /**
     * 更新章节
     * @param lesson 章节信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/update")
    fun updateLesson(@RequestBody lesson: Lesson): BaseResult {
        return lessonService.updateLesson(lesson)
    }
    
    /**
     * 删除章节
     * @param lessonId 章节ID
     * @return 操作结果
     */
    @PassToken
    @DeleteMapping("/delete/{lessonId}")
    fun deleteLesson(@PathVariable lessonId: Int): BaseResult {
        return lessonService.deleteLesson(lessonId)
    }
    
    /**
     * 获取章节详情
     * @param lessonId 章节ID
     * @return 章节详情
     */
    @PassToken
    @GetMapping("/detail/{lessonId}")
    fun getLessonById(@PathVariable lessonId: Int): BaseResult {
        return lessonService.getLessonById(lessonId)
    }
    
    /**
     * 获取课程的所有章节
     * @param courseId 课程ID
     * @return 章节列表
     */
    @PassToken
    @GetMapping("/course/{courseId}")
    fun getLessonsByCourseId(@PathVariable courseId: Int): BaseResult {
        return lessonService.getLessonsByCourseId(courseId)
    }
    
    /**
     * 分页获取课程章节
     * @param courseId 课程ID
     * @param reqBody 请求参数
     * @return 分页章节列表
     */
    @PassToken
    @PostMapping("/course/{courseId}/page")
    fun getLessonsByCourseIdWithPage(
        @PathVariable courseId: Int,
        @RequestBody reqBody: ReqBody
    ): BaseResult {
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
        return lessonService.getLessonsByCourseIdWithPage(courseId, pageBody)
    }
}