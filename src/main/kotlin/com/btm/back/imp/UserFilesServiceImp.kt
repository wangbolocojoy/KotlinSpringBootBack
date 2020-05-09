package com.btm.back.imp

import com.btm.back.bean.FileBody
import com.btm.back.bean.ReqBody
import com.btm.back.dto.UserFiles
import com.btm.back.repository.UserFilesRespository
import com.btm.back.service.UserFilesService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserFilesServiceImp:UserFilesService{
    @Autowired
    lateinit var userFilesRespository: UserFilesRespository

    override fun saveFiles(body: FileBody): BaseResult {

        var userfile = UserFiles()

        return BaseResult.SECUESS()
    }

    override fun getfileByFileId(body: ReqBody): BaseResult {
        return BaseResult.SECUESS()
    }

    override fun getfileByUserId(body: ReqBody): BaseResult {
        return BaseResult.SECUESS()
    }

    override fun deleteFileByFileId(body: ReqBody): BaseResult {
        return BaseResult.SECUESS()
    }

}
