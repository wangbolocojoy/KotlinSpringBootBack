package com.btm.back.service

import com.btm.back.bean.FileBody
import com.btm.back.bean.ReqBody
import com.btm.back.utils.BaseResult
import org.springframework.web.multipart.MultipartFile

interface UserFilesService {

    fun uploadFiles(userId: Int?,latitude:String?,longitude:String?,postPublic:Boolean?,postDetail:String?,postAddress:String?,uploadType:String?,uploadFile: ArrayList<MultipartFile>?):BaseResult
}
