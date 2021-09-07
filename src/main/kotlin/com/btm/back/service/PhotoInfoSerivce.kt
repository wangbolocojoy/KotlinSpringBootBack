package com.btm.back.service

import com.btm.back.bean.PhotoBody
import com.btm.back.bean.PhotoInfoBody
import com.btm.back.utils.BaseResult

interface PhotoInfoSerivce {
    fun getPhotoByType(body: PhotoBody): BaseResult
    fun getAllPhotos(body: PhotoBody): BaseResult
    fun getAllPhotoTypes():BaseResult
    fun addPhoto(body: PhotoInfoBody): BaseResult
}