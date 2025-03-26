package com.btm.back.imp

import com.btm.back.dto.Blogfiles
import com.btm.back.repository.ArticleRepository
import com.btm.back.repository.BlogfilesRepository
import com.btm.back.service.BlogfilesService
import com.btm.back.utils.AliYunOssUtil
import com.btm.back.utils.BaseResult
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * Blogfiles服务实现类，提供与博客文件相关的操作
 * 该类标记为@Service，表示它是一个服务层组件
 * @Transactional注解表明该类中的方法将使用事务管理
 */
@Service
@Transactional
class BlogfilesServiceImpl : BlogfilesService {
    // 日志记录器，用于记录日志信息
    private val logger = LoggerFactory.getLogger(ArticleServiceImpl::class.java)

    // BlogfilesRepository接口的实例，用于访问数据库中的博客文件数据
    @Autowired
    lateinit var blogfilesRepository: BlogfilesRepository

    /**
     * 创建新的博客文件
     * 此方法尚未实现
     * @param blogfile 要创建的博客文件对象
     * @return 创建结果，包括成功或失败的信息
     */
    override fun createBlogfile(uploadFile: MultipartFile?): BaseResult {
       return if (uploadFile?.isEmpty == true){
           BaseResult.FAIL("文件为空")
       }else{
           logger.info("文件名uploadFile?.originalFilename${uploadFile?.originalFilename}")
           logger.info("文件名uploadFile?.name${uploadFile?.name}")
           logger.info("文件名uploadFile?.contentType${uploadFile?.contentType}")
           logger.info("文件名uploadFile?.size${uploadFile?.size}")
           logger.info("文件名uploadFile?.bytes${uploadFile?.bytes}byte")
           val fileurl =  AliYunOssUtil.uploadToAliyun(
                     uploadFile?.name ?: "", uploadFile?.inputStream, "",
                       uploadFile?.contentType ?: "jpg", "")
           if (fileurl != ""){
               val blogfile = Blogfiles()
               blogfile.fileType = uploadFile?.contentType
               blogfile.originalFileName = uploadFile?.originalFilename
               blogfile.fileUrl = fileurl
               blogfile.createdAt = Date()
               blogfilesRepository.save(blogfile)
               BaseResult.SUCCESS("上传成功",blogfile)
           }else{
               BaseResult.FAIL("文件上传失败")
           }

       }
    }



}
