package com.btm.back.controller

import com.btm.back.bean.PageBody
import com.btm.back.dto.Photography
import com.btm.back.imp.PhotographyServiceImpl
import com.btm.back.utils.BaseResult
import com.btm.back.utils.PassToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * 摄影作品控制器
 * @author Trae AI
 * @date 2023-06-01
 */
@RestController
@RequestMapping("/api/photography")
class PhotographyController {

    @Autowired
    lateinit var photographyService: PhotographyServiceImpl
    
    /**
     * 创建摄影作品
     * @param photography 摄影作品信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/create")
    fun createPhotography(@RequestBody photography: Photography): BaseResult {
        return photographyService.createPhotography(photography)
    }
    
    /**
     * 更新摄影作品
     * @param photography 摄影作品信息
     * @return 操作结果
     */
    @PassToken
    @PostMapping("/update")
    fun updatePhotography(@RequestBody photography: Photography): BaseResult {
        return photographyService.updatePhotography(photography)
    }
    
    /**
     * 删除摄影作品
     * @param photographyId 摄影作品ID
     * @return 操作结果
     */
    @PassToken
    @DeleteMapping("/delete/{photographyId}")
    fun deletePhotography(@PathVariable photographyId: Int): BaseResult {
        return photographyService.deletePhotography(photographyId)
    }
    
    /**
     * 获取摄影作品详情
     * @param photographyId 摄影作品ID
     * @return 摄影作品详情
     */
    @PassToken
    @GetMapping("/detail/{photographyId}")
    fun getPhotographyById(@PathVariable photographyId: Int): BaseResult {
        return photographyService.getPhotographyById(photographyId)
    }
    
    /**
     * 分页获取所有摄影作品
     * @param pageBody 分页参数
     * @return 摄影作品列表
     */
    @PassToken
    @PostMapping("/list")
    fun getAllPhotography(@RequestBody pageBody: PageBody): BaseResult {
        return photographyService.getAllPhotography(pageBody)
    }
    
    /**
     * 根据作者获取摄影作品列表
     * @param author 作者名称
     * @param pageBody 分页参数
     * @return 摄影作品列表
     */
    @PassToken
    @PostMapping("/list/author/{author}")
    fun getPhotographyByAuthor(@PathVariable author: String, @RequestBody pageBody: PageBody): BaseResult {
        return photographyService.getPhotographyByAuthor(author, pageBody)
    }
}