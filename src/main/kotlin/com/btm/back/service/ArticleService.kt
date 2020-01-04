package com.btm.back.service

import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult

interface ArticleService {

    fun creatArticle(body: ReqBody?): BaseResult
    fun getArticleDetailById(body: ReqBody?): BaseResult
    fun getArticleByType(body: ReqBody?): BaseResult
    fun getArticleList(body: ReqBody?): BaseResult
    fun updateArticle(body: ReqBody?): BaseResult
    fun deleteArticleById(body: ReqBody?): BaseResult

}
