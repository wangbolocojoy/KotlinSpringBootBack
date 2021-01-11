package com.btm.back.service

import com.btm.back.bean.PostBody
import com.btm.back.utils.BaseResult

interface FavoritesService {
    fun collection(body: PostBody): BaseResult
    fun cancelCollection(body: PostBody): BaseResult
    fun getCollectionList(body: PostBody): BaseResult

}
