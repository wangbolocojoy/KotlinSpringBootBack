package com.btm.back.service

import com.btm.back.bean.PhotoBody
import com.btm.back.bean.PhotoInfoBody
import com.btm.back.utils.BaseResult

/**
 * 照片信息服务接口
 * 负责照片信息的增删改查操作
 * @author Trae AI
 * @date 2023-06-01
 */
interface PhotoInfoService {
    /**
     * 根据类型获取照片
     * @param body 包含照片类型的请求体
     * @return 照片列表
     */
    fun getPhotoByType(body: PhotoBody): BaseResult
    
    /**
     * 获取所有照片
     * @param body 请求体
     * @return 所有照片列表
     */
    fun getAllPhotos(body: PhotoBody): BaseResult
    
    /**
     * 获取所有照片类型
     * @return 照片类型列表
     */
    fun getAllPhotoTypes():BaseResult
    
    /**
     * 添加照片
     * @param body 照片信息请求体
     * @return 操作结果
     */
    fun addPhoto(body: PhotoInfoBody): BaseResult
}