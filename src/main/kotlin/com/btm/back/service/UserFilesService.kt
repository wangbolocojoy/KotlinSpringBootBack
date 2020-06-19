package com.btm.back.service

import com.btm.back.bean.FileBody
import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult
import org.springframework.web.multipart.MultipartFile

interface UserFilesService {
    fun saveFiles(body: FileBody):BaseResult
    fun getfileByFileId(body: ReqBody):BaseResult
    fun getfileByUserId(body: ReqBody):BaseResult
    fun deleteFileByFileId(body: ReqBody):BaseResult
    fun uploadFiles(id: Int?,postid:Int?,uploadType:String?,uploadFile: ArrayList<MultipartFile>?):BaseResult
}
