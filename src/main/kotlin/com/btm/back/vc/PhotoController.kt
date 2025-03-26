package com.btm.back.vc

import com.btm.back.bean.PageBody
import com.btm.back.bean.PhotoBody
import com.btm.back.imp.PhotoInfoServiceImpl
import com.btm.back.imp.PostServiceIml
import com.btm.back.utils.PassToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/10kraw/photo"])
class PhotoController {
    @Autowired
    lateinit var photoInfoServiceIml: PhotoInfoServiceImpl


    /**
     * 获取照片分类列表
     */
    @PassToken
    @RequestMapping(value = ["getAllPhotoTypes"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getAllPhotoTypes() = photoInfoServiceIml.getAllPhotoTypes()

    /**
     * 按分类获取照片
     */
    @PassToken
    @RequestMapping(value = ["getPhotoByType"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getPhotoByType(@RequestBody body: PhotoBody) = photoInfoServiceIml.getPhotoByType(body)

    /**
     * 获取所有照片
     */
    @PassToken
    @RequestMapping(value = ["getAllPhotos"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun getAllPhotos(@RequestBody body: PhotoBody) = photoInfoServiceIml.getAllPhotos(body)




}