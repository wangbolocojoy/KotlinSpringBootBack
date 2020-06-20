package com.btm.back.imp

import com.btm.back.bean.FileBody
import com.btm.back.bean.ReqBody
import com.btm.back.dto.UserFiles
import com.btm.back.repository.UserFilesRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.UserFilesService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserFilesServiceImp:UserFilesService{
    @Autowired
    lateinit var userFilesRespository: UserFilesRespository

    @Autowired
    lateinit var userrepository: UserRespository

    private val logger: Logger = LoggerFactory.getLogger(UserFilesServiceImp::class.java)

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

    override fun uploadFiles(id: Int?,postid:Int?, uploadType: String?, uploadFile: ArrayList<MultipartFile>?): BaseResult {
        return if (id == null || uploadType == null ||postid ==null){
            BaseResult.FAIL("参数不能为空")
        }else{
            val list = AliYunOssUtil.uploadToAliyunFiles(id,postid,userFilesRespository,uploadFile = uploadFile,userid = id.toString())
            BaseResult.SECUESS("上传"+list.size+"张图片成功",list)
        }
    }

}
