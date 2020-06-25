package com.btm.back.imp

import com.btm.back.bean.FileBody
import com.btm.back.bean.ReqBody
import com.btm.back.dto.Post
import com.btm.back.dto.UserFiles
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.PostRespository
import com.btm.back.repository.UserFilesRespository
import com.btm.back.repository.UserRespository
import com.btm.back.service.UserFilesService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import com.btm.back.vo.PostVO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Service
class UserFilesServiceImp:UserFilesService{
    @Autowired
    lateinit var userFilesRespository: UserFilesRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var postRespository: PostRespository

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

    override fun uploadFiles(userId: Int?,latitude:String?,longitude:String?, postPublic:Boolean?, postDetail: String?, postAddress: String?, uploadType: String?, uploadFile: ArrayList<MultipartFile>?): BaseResult {
        return if (userId == null || uploadType == null ||postDetail == null || uploadFile == null){
            BaseResult.FAIL("参数不能为空")
        }else{
            val post = Post()
            post.userId = userId
            post.postAddress = postAddress
            post.postDetail = postDetail
            post.postPublic = postPublic ?: false
            post.postStarts =  0
            post.creatTime = Date(System.currentTimeMillis())
            post.latitude = latitude
            post.longitude = longitude
            postRespository.save(post)
            val user = userRespository.findById(userId )
            user?.postNum =(user?.postNum?:0)+1
            user?.let { userRespository.save(it) }
            logger.info("post---  ",post.id)
            if (post.id != null){
            val list = AliYunOssUtil.uploadToAliyunFiles(userId,post.id ?:0,userFilesRespository,uploadFile = uploadFile,userid = userId.toString())
                logger.info("上传成功$list")
                BaseResult.SECUESS("发帖成功")
            }else{
                return  BaseResult.FAIL("发帖失败")
            }

        }
    }

}
