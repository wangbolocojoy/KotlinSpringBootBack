package com.btm.back.service

import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult
import org.springframework.stereotype.Service

@Service
interface NovelTypeService {
    fun getNovelTypes(body: ReqBody?):BaseResult
}
