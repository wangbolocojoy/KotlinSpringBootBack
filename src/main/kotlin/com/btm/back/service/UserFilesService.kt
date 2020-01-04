package com.btm.back.service

import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult

interface UserFilesService {
    fun saveFiles(body: ReqBody):BaseResult
    fun getfileByFileId(body: ReqBody):BaseResult
    fun getfileByUserId(body: ReqBody):BaseResult
    fun deleteFileByFileId(body: ReqBody):BaseResult

}
