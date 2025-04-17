package com.btm.back.controller

import com.btm.back.imp.ArticleServiceImpl
import com.btm.back.repository.*
import com.btm.back.service.BlogfilesService
import com.btm.back.utils.BaseResult
import com.btm.back.utils.PassToken
import com.btm.back.utils.UserLoginToken
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/home")
class HomeController {
    @Autowired
    lateinit var articleRepository: ArticleRepository
    @Autowired
    lateinit var classicalArticleRepository: ClassicalArticleRepository
    @Autowired
    lateinit var photographyRepository: PhotographyRepository
    @Autowired
    lateinit var lessonRepository: LessonRepository
    @Autowired
    lateinit var blogfilesService: BlogfilesService


    @PassToken
    @PostMapping("/get-home")
    fun getHomeData(): BaseResult {
        var articleList = articleRepository.findAll()
        var classicalArticleList = classicalArticleRepository.findAll()
        var photographyList = photographyRepository.findAll()
        var lessonList = lessonRepository.findAll()
        var data = mapOf(
            "articleList" to articleList,
            "classicalArticleList" to classicalArticleList,
            "photographyList" to photographyList,
            "lessonList" to lessonList

        )
        return BaseResult.SUCCESS("获取首页数据成功", data)
    }
    @PassToken
    @RequestMapping(value = ["uploadfile"], method = [RequestMethod.POST])
    fun uploadFile(@RequestPart("imageurl")uploadFile: MultipartFile?):BaseResult {
        return  blogfilesService.createBlogfile(uploadFile)
    }

}