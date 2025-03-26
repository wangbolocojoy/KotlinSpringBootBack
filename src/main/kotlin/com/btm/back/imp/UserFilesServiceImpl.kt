package com.btm.back.imp

import com.btm.back.bean.PageBody
import com.btm.back.dto.Post
import com.btm.back.helper.CopierUtil
import com.btm.back.repository.PostRepository
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
import java.util.*
import kotlin.collections.ArrayList
@Transactional
@Service
/**
 * 用户文件服务实现类
 * 负责用户文件的上传、查询等操作
 * @author Trae AI
 * @date 2023-06-01
 */
class UserFilesServiceImpl : UserFilesService {
    @Autowired
    lateinit var userFilesRespository: UserFilesRespository

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var postRespository: PostRepository

    private val logger: Logger = LoggerFactory.getLogger(UserFilesServiceImpl::class.java)

    /**
     * 上传文件并创建帖子
     * @param userId 用户ID
     * @param latitude 纬度
     * @param longitude 经度
     * @param postPublic 是否公开
     * @param postDetail 帖子详情
     * @param postAddress 帖子地址
     * @param uploadType 上传类型
     * @param uploadFile 上传的文件列表
     * @return 操作结果
     */
    override fun uploadFiles(userId: Int?,latitude:String?,longitude:String?, postPublic:Boolean?, postDetail: String?, postAddress: String?, uploadType: String?, uploadFile: ArrayList<MultipartFile>?): BaseResult {
        return if (userId == null || uploadType == null ||postDetail == null || uploadFile == null){
            BaseResult.FAIL("参数不能为空")
        }else{
            if (AliYunOssUtil.checkContext(postDetail)){
                val post = Post()
                post.userId = userId
                post.postAddress = postAddress
                post.postDetail = postDetail
                post.postPublic = postPublic ?: true
                post.postStarts =  0
                post.creatTime = Date()
                post.latitude = latitude
                post.longitude = longitude
                post.postState = 1
                post.postMessageNum = 0
                post.postReport = 0
                postRespository.save(post)
                val user = userRespository.findById(userId)
                user?.postNum =(user?.postNum?:0)+1
                user?.let { userRespository.save(it) }
                logger.info("post---  ",post.id)
                if (post.id != null){
                    val list = AliYunOssUtil.uploadToAliyunFiles(userId,post.id ?:0,userFilesRespository, uploadFile,userid = userId.toString())
                    logger.info("上传成功$list")
                    val pvo = CopierUtil.copyProperties(post,PostVO::class.java)
                    pvo?.postImages = list
                    pvo?.isStart = false
                    pvo?.isCollection = false
                    pvo?.msgNum = 0
                    BaseResult.SUCCESS(pvo)

                }else{
                    return  BaseResult.FAIL("发帖失败")
                }
            }else{
                return  BaseResult.FAIL("内容违规,请重新组织语言")
            }


        }
    }

    /**
     * 获取用户所有图片
     * @param body 请求体，包含用户ID和分页信息
     * @return 图片列表
     */
    override fun getMyAllImages(body: PageBody): BaseResult {
        val pageable: Pageable = PageRequest.of(body.page ?: 0, body.pageSize ?: 10)
        val images = body.userId?.let { userFilesRespository.findByUserIdOrderByIdDesc(it,pageable) } ?: BaseResult.FAIL()
        return BaseResult.SUCCESS(images)
    }


}
