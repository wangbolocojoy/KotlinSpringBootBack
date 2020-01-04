package com.btm.back.service

import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult

interface NovelService {
    fun getPageNovelList(body: ReqBody?):BaseResult
    fun searchNovel(body: ReqBody?):BaseResult
    fun getallnoveltype(body: ReqBody?):BaseResult
}
