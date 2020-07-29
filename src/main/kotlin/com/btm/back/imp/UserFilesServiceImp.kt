package com.btm.back.imp

import com.btm.back.bean.FileBody
import com.btm.back.bean.PageBody
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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
@Transactional
@Service
class UserFilesServiceImp:UserFilesService{
    @Autowired
    lateinit var userFilesRespository: UserFilesRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var postRespository: PostRespository

    private val logger: Logger = LoggerFactory.getLogger(UserFilesServiceImp::class.java)

    /**
    * @Description: 发帖
    * @Param:
    * @return:
    * @Author: hero
    * @Date: 2020-06-26
    * @Time: 01:26
    **/
    override fun uploadFiles(userId: Int?,latitude:String?,longitude:String?, postPublic:Boolean?, postDetail: String?, postAddress: String?, uploadType: String?, uploadFile: ArrayList<MultipartFile>?): BaseResult {
        return if (userId == null || uploadType == null ||postDetail == null || uploadFile == null){
            BaseResult.FAIL("参数不能为空")
        }else{
            if (AliYunOssUtil.checkContext(postDetail)){
                val post = Post()
                post.userId = userId
                post.postAddress = postAddress
                post.postDetail = postDetail
                post.postPublic = postPublic ?: false
                post.postStarts =  0
                post.creatTime = Date()
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
                    val pvo = CopierUtil.copyProperties(post,PostVO::class.java)
                    pvo?.postImages = list
                    pvo?.isStart = false
                    pvo?.isCollection = false
                    pvo?.msgNum = 0
                    BaseResult.SECUESS(pvo)

                }else{
                    return  BaseResult.FAIL("发帖失败")
                }
            }else{
                return  BaseResult.FAIL("内容违规,请重新组织语言")
            }


        }
    }

    override fun getMyAllImages(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val images = body.userId?.let { userFilesRespository.findByUserIdOrderByIdDesc(it,pageable) } ?: BaseResult.FAIL()
        return BaseResult.SECUESS(images)
    }


}
