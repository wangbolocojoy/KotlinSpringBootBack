package com.btm.back.service

import com.btm.back.bean.PageBody
import com.btm.back.dto.Photography
import com.btm.back.utils.BaseResult

interface PhotographyService {
    fun photography()
    fun getAllPhotography(pageBody: PageBody): BaseResult
    fun getPhotographyByAuthor(author: String, pageBody: PageBody): BaseResult
    fun getLatestPhotographs(page: Int): BaseResult
    fun getPhotographyById(photographyId: Int): BaseResult
    fun updatePhotography(photography: Photography): BaseResult
    fun deletePhotography(photographyId: Int): BaseResult
    fun createPhotography(photography: Photography): BaseResult
}